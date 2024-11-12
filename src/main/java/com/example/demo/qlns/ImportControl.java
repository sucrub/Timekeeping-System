package com.example.demo.qlns;

import com.example.demo.DbUtil;
import com.example.demo.entity.User;
import com.example.demo.log.LogControl;
import com.example.demo.log.OfficerLogControl;
import com.example.demo.log.WorkerLogControl;
import com.example.demo.qlnssystem.IQLNSSystem;
import com.example.demo.qlnssystem.QLNSSystemData;
import javafx.scene.control.Alert;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ImportControl {
    LogControl logControl = new LogControl();
    WorkerLogControl WorkerLogControl = new WorkerLogControl();
    OfficerLogControl OfficerLogControl = new OfficerLogControl();
    IQLNSSystem iqlnsSystem = new QLNSSystemData();
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String openMorning = "08:00";
    private static final String endMorning = "12:00";
    private static final String openAfternoon = "13:00";
    private static final String endAfternoon = "17:00";
    private static final String openEvening = "18:00";
    private static final String endEvening = "22:00";

    public void ImportLogs(File selectedFile) throws IOException {
        if (selectedFile != null) {
            if (!validateFile(selectedFile)) {
                showAlert("Invalid Excel File", "Please select a valid Excel file with .xls or .xlsx extension.");
                return;
            }

            if (!isValidFileSize(selectedFile)) {
                showAlert("Invalid Excel File", "Maximum of file size is 10MB.");
                return;
            }

            try {
                var success = importData(selectedFile);
                if (success) {
                    showSuccess("Import", "Success");
                }
            } catch (IOException | IllegalStateException | NumberFormatException e) {
                e.printStackTrace();
                // Handle exception as needed
            }
        }
    }

    public static boolean isValidFileSize(File file) {
        return file.length() <= MAX_FILE_SIZE;
    }

    public static boolean validateFile(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             Workbook ignored = new XSSFWorkbook(fis)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean importData(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {
            List<User> workers = iqlnsSystem.getUsersByDepartment(1);
            int[] workerIds = getIdsFromUsers(workers);

            List<User> officers = iqlnsSystem.getUsersIsOfficer();
            int[] officerIds = getIdsFromUsers(officers);

            Map<String, Map<Integer, List<Map<String, List<Date>>>>> workerLogs = new HashMap<>();
            Map<String, Map<Integer, List<Map<String, List<Date>>>>> officerLogs = new HashMap<>();

            Connection connection = DbUtil.getConnection();
            connection.setAutoCommit(false);
            try {
                insertDataIntoDatabase(connection, workbook, "checkIn", workerIds, officerIds, workerLogs, officerLogs);
                insertDataIntoDatabase(connection, workbook, "checkOut", workerIds, officerIds, workerLogs, officerLogs);
                insertWorkerLogsIntoDatabase(connection, workerLogs);
                createOfficerLogIntoDatabase(connection, officerLogs);
                connection.commit();
                return true;
            } catch (SQLException e) {
                connection.rollback();
                showAlert("Transaction Error", e.getMessage());
                return false;
            }
        } catch (IOException | SQLException e) {
            showAlert("Error", "An error has been occurred, try again later.");
            return false;
        }
    }

    private void createOfficerLogIntoDatabase(Connection connection, Map<String, Map<Integer, List<Map<String, List<Date>>>>> officerLogs) throws SQLException {
        for (Map.Entry<String, Map<Integer, List<Map<String, List<Date>>>>> entry : officerLogs.entrySet()) {
            Map<Integer, List<Map<String, List<Date>>>> userLogs = entry.getValue();

            for (Map.Entry<Integer, List<Map<String, List<Date>>>> userEntry : userLogs.entrySet()) {
                int userId = userEntry.getKey();
                List<Map<String, List<Date>>> logs = userEntry.getValue();
                if ((long) logs.size() != 2) {
                    throw new SQLException("UserId "+ userId + " missing checkin or checkout");
                }
                Map<String, List<Date>> checkin = logs.get(0);
                List<Date> checkinStamp = null;
                for (Map.Entry<String, List<Date>> checkinLogs : checkin.entrySet()) {
                    checkinStamp = checkinLogs.getValue();
                }
                Map<String, List<Date>> checkOut = logs.get(1);
                List<Date> checkOutStamp = null;
                for (Map.Entry<String, List<Date>> checkOutLogs : checkOut.entrySet()) {
                    checkOutStamp = checkOutLogs.getValue();
                }

                if (checkinStamp == null ||
                        checkOutStamp == null ||
                        checkinStamp.size() != checkOutStamp.size()) {
                    throw new SQLException("UserId "+ userId + " missing checkin or checkout");
                }
                Collections.sort(checkinStamp);
                Collections.sort(checkOutStamp);
                Map<LocalDate, List<Date>> groupedByDateCheckIn = checkinStamp.stream()
                        .collect(Collectors.groupingBy(ImportControl::convertToLocalDate));
                Map<LocalDate, List<Date>> groupedByDateCheckOut = checkOutStamp.stream()
                        .collect(Collectors.groupingBy(ImportControl::convertToLocalDate));

                executeCreateOfficerLog(connection, groupedByDateCheckIn, groupedByDateCheckOut, userId);
            }
        }
    }

    private void executeCreateOfficerLog(Connection connection, Map<LocalDate, List<Date>> groupedByDate, Map<LocalDate, List<Date>> groupedByDateCheckOut, int userId) throws SQLException {
        for (Map.Entry<LocalDate, List<Date>> entry : groupedByDate.entrySet()) {
            LocalDate localDate = entry.getKey();
            List<Date> checkin = entry.getValue();
            List<Date> checkout = groupedByDateCheckOut.get(localDate);
            var logCount = checkin.size();
            if (logCount > 2) {
                throw new SQLException("UserId "+ userId + " has over limit checkin checkout");
            }
            List<Boolean> booleanList = Arrays.asList(false, false);
            var shiftDefine = getShiftDefine(localDate);
            for (int i = 0; i < logCount; i++) {
                if (checkin.get(i).compareTo(checkout.get(i)) > 0) {
                    throw new SQLException("UserId " + userId + " has invalid log");
                }
                booleanList.set(i, isDateRangeIntersect(checkin.get(i), checkout.get(i), shiftDefine.get(2 * i), shiftDefine.get(2 * i + 1)));
            }

            double timeLate = 0.0;
            double timeEarly = 0.0;

            for (int i = 0; i < logCount; i++) {
                if (!booleanList.get(i)) {
                    continue;
                }
                timeLate += Math.max(getDistanceHour(shiftDefine.get(2 * i), checkin.get(i)), 0.0);
                timeEarly += Math.max(getDistanceHour(checkout.get(i), shiftDefine.get(2 * i + 1)), 0.0);
            }
            var officerLog = OfficerLogControl.getOfficerLogByUserIdAndDate(connection, userId, localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            if (officerLog == null) {
                OfficerLogControl.createOfficerLog(connection, localDate, booleanList, timeLate, timeEarly, userId);
            } else {
                OfficerLogControl.editOfficerLog(connection, officerLog.getId(), booleanList, timeLate, timeEarly);
            }
        }
    }

    private void insertWorkerLogsIntoDatabase(Connection connection, Map<String, Map<Integer, List<Map<String, List<Date>>>>> workerLogs) throws SQLException {
        for (Map.Entry<String, Map<Integer, List<Map<String, List<Date>>>>> entry : workerLogs.entrySet()) {
            Map<Integer, List<Map<String, List<Date>>>> userLogs = entry.getValue();

            for (Map.Entry<Integer, List<Map<String, List<Date>>>> userEntry : userLogs.entrySet()) {
                int userId = userEntry.getKey();
                List<Map<String, List<Date>>> logs = userEntry.getValue();
                if ((long) logs.size() != 2) {
                    throw new SQLException("UserId "+ userId + " missing checkin or checkout");
                }
                Map<String, List<Date>> checkin = logs.get(0);
                List<Date> checkinStamp = null;
                for (Map.Entry<String, List<Date>> checkinLogs : checkin.entrySet()) {
                    checkinStamp = checkinLogs.getValue();
                }
                Map<String, List<Date>> checkOut = logs.get(1);
                List<Date> checkOutStamp = null;
                for (Map.Entry<String, List<Date>> checkOutLogs : checkOut.entrySet()) {
                    checkOutStamp = checkOutLogs.getValue();
                }

                if (checkinStamp == null ||
                        checkOutStamp == null ||
                        checkinStamp.size() != checkOutStamp.size()) {
                    throw new SQLException("UserId "+ userId + " missing checkin or checkout");
                }
                Collections.sort(checkinStamp);
                Collections.sort(checkOutStamp);
                Map<LocalDate, List<Date>> groupedByDateCheckIn = checkinStamp.stream()
                        .collect(Collectors.groupingBy(ImportControl::convertToLocalDate));
                Map<LocalDate, List<Date>> groupedByDateCheckOut = checkOutStamp.stream()
                        .collect(Collectors.groupingBy(ImportControl::convertToLocalDate));

                executeCreateWorkerLog(connection, groupedByDateCheckIn, groupedByDateCheckOut, userId);
            }
        }
    }

    private void executeCreateWorkerLog(Connection connection, Map<LocalDate, List<Date>> groupedByDate, Map<LocalDate, List<Date>> groupedByDateCheckOut, int userId) throws SQLException {
        for (Map.Entry<LocalDate, List<Date>> entry : groupedByDate.entrySet()) {
            LocalDate localDate = entry.getKey();
            List<Date> checkin = entry.getValue();
            List<Date> checkOut = groupedByDateCheckOut.get(localDate);
            var logCount = checkin.size();
            if (logCount > 3) {
                throw new SQLException("UserId "+ userId + " has over limit checkin checkout");
            }
            List<Double> doubleList = Arrays.asList(0.0, 0.0, 0.0);
            for (int i = 0; i < logCount; i ++) {
                if (checkin.get(i).compareTo(checkOut.get(i)) > 0){
                    throw new SQLException("UserId "+ userId + " has checkin later than checkout");
                }
                doubleList.set(i, Math.min(getDistanceHour(checkin.get(i), checkOut.get(i)), 4.0));
            }

            var workerLog = WorkerLogControl.getWorkerLogByUserIdAndDate(connection, userId, localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            if (workerLog == null) {
                WorkerLogControl.createWorkerLog(connection, localDate, doubleList, userId);
            } else {
                WorkerLogControl.editWorkerLog(connection, workerLog.getId(), doubleList);
            }
        }
    }


    private void insertDataIntoDatabase(Connection connection,
                                        Workbook workbook,
                                        String sheetName,
                                        int[] workerIds,
                                        int[] officerIds,
                                        Map<String, Map<Integer, List<Map<String, List<Date>>>>> workerLogs,
                                        Map<String, Map<Integer, List<Map<String, List<Date>>>>> officerLogs) throws SQLException {
        Sheet sheet = workbook.getSheet(sheetName);

        if (sheet == null) {
            showAlert("Sheet Not Found", "Sheet '" + sheetName + "' not found in the Excel file.");
            return;
        }

        for (Row row : sheet) {
            Cell userIdCell = row.getCell(0);
            Cell timestampCell = row.getCell(1);

            if (userIdCell == null || timestampCell == null) {
                showAlert("Invalid content", "You have a row missing userId or log.");
                return;
            }
            if (userIdCell.getCellType() != CellType.NUMERIC) {
                throw new SQLException("UserId must be integer");
            }
            int userId = (int) userIdCell.getNumericCellValue();
            if (Arrays.binarySearch(workerIds, userId) < 0 && Arrays.binarySearch(officerIds, userId) < 0) {
                throw new SQLException("Unknown userId: " + userId);
            }
            try {
                Date timestamp = timestampCell.getDateCellValue();
                logControl.createLog(connection, userId, new Timestamp(timestamp.getTime()));
                if (Arrays.binarySearch(workerIds, userId) >= 0) {
                    logData(workerLogs, userId, timestamp, sheetName);
                } else {
                    logData(officerLogs, userId, timestamp, sheetName);
                }
            } catch (IllegalStateException | NumberFormatException e) {
                throw new SQLException("Invalid timestamp for userId: " + userId + " at sheet  " + sheetName);
            }
        }
    }

    private static int[] getIdsFromUsers(List<User> users) {
        return users.stream().mapToInt(User::getId).toArray();
    }

    private static void logData(Map<String, Map<Integer, List<Map<String, List<Date>>>>> mapLogs,
                                int userId,
                                Date timestamp,
                                String sheetName) {
        mapLogs.putIfAbsent("logs", new HashMap<>());

        Map<Integer, List<Map<String, List<Date>>>> userLogs = mapLogs.get("logs");
        userLogs.putIfAbsent(userId, new ArrayList<>());

        List<Map<String, List<Date>>> checkLogs = userLogs.get(userId);

        Map<String, List<Date>> dateMap = checkLogs.stream()
                .filter(map -> map.containsKey(sheetName))
                .findFirst()
                .orElseGet(() -> {
                    Map<String, List<Date>> newMap = new HashMap<>();
                    newMap.put(sheetName, new ArrayList<>());
                    checkLogs.add(newMap);
                    return newMap;
                });

        List<Date> timestamps = dateMap.get(sheetName);
        timestamps.add(timestamp);
    }

    private static Date getShiftTime(String defineShift, LocalDate localDate) {
        LocalTime localTime = LocalTime.parse(defineShift);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);

        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private static List<Date> getShiftDefine(LocalDate localDate) {
        return List.of(
                getShiftTime(openMorning, localDate),
                getShiftTime(endMorning, localDate),
                getShiftTime(openAfternoon, localDate),
                getShiftTime(endAfternoon, localDate),
                getShiftTime(openEvening, localDate),
                getShiftTime(endEvening, localDate));
    }

    private static boolean isDateRangeIntersect(Date start1, Date end1, Date start2, Date end2) {
        return start1.before(end2) && end1.after(start2);
    }

    private static LocalDate convertToLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static double getDistanceHour(Date startDate, Date endDate) {
        long timeDifferenceMillis = endDate.getTime() - startDate.getTime();
        return (double) timeDifferenceMillis / (60 * 60 * 1000);
    }

    private static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private static void showSuccess(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
