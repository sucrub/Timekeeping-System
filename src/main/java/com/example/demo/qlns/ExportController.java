package com.example.demo.qlns;

import com.example.demo.entity.Department;
import com.example.demo.entity.OfficerLog;
import com.example.demo.entity.User;
import com.example.demo.entity.WorkerLog;
import com.example.demo.qlnssystem.IQLNSSystem;
import com.example.demo.qlnssystem.QLNSSystemData;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ExportController {

    public List<User> getUserList() {
        return userList;
    }

    private final List<User> userList;
    private List<OfficerLog> officerLog;
    private List<WorkerLog> workerLog;
    private final int month;
    private final int departmentId;
    private final int day;

    IQLNSSystem iqlnsSystem = new QLNSSystemData();

    public ExportController(List<User> userList, List<OfficerLog> officerLog, List<WorkerLog> workerLog, int month, int departmentId, int day) {
        this.userList = userList;
        this.officerLog = officerLog;
        this.workerLog = workerLog;
        this.month = month;
        this.departmentId = departmentId;
        this.day = day;
    }

    public String getDepartmentName() throws SQLException {
        Department data = iqlnsSystem.getDepartmentById(departmentId);
        return data.getName();
    }

    public int getCurrentMonth() {
        return month;
    }

    private boolean containsId(List<User> users, int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return false;
            }
        }
        return true;
    }

    private List<OfficerLog> filterOfficerLog(List<User> filterUser) {
        officerLog.removeIf(log -> containsId(filterUser, log.getId()));
        return officerLog;
    }

    private List<WorkerLog> filterWorkerLog(List<User> filterUser) {
        workerLog.removeIf(log -> containsId(filterUser, log.getId()));
        return workerLog;
    }

    public void exportData(List<User> users, int fileTypeId) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File Location");
        // Change data type here
        if(fileTypeId == 1) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        }
        else {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        }

        // Show save file dialog
        java.io.File file = fileChooser.showSaveDialog(null);

        if (file == null) {
            System.out.println("File selection canceled.");
            return;
        }

        String FILE_PATH = file.getAbsolutePath();

        if(departmentId != 1) {
            officerLog = filterOfficerLog(users);
        }
        else {
            workerLog = filterWorkerLog(users);
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            // Create a sheet for users
            Sheet userSheet = workbook.createSheet("Logs");
            createHeaderRowForUsers(userSheet);

            // Fill data for users
            int userRowIndex = 1;
            int userIndex = 0;
            if(departmentId != 1) {
                for (User user : users) {
                    Row row = userSheet.createRow(userRowIndex++);
                    fillMorningData(row, userIndex, user);
                    Row row1 = userSheet.createRow(userRowIndex++);
                    fillAfternoonData(row1, userIndex);
                    Row row2 = userSheet.createRow(userRowIndex++);
                    fillLateData(row2, userIndex);
                    Row row3 = userSheet.createRow(userRowIndex++);
                    fillEarlyData(row3, userIndex);
                    userIndex++;
                }
            }
            else {
                for (User user : users) {
                    Row row = userSheet.createRow(userRowIndex++);
                    fillShift1Data(row, userIndex, user);
                    Row row1 = userSheet.createRow(userRowIndex++);
                    fillShift2Data(row1, userIndex);
                    Row row2 = userSheet.createRow(userRowIndex++);
                    fillShift3Data(row2, userIndex);
                    userIndex++;
                }
            }

            // Write the workbook to the file
            try (FileOutputStream fileOut = new FileOutputStream(FILE_PATH)) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createHeaderRowForUsers(Sheet sheet) {
        // set column
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Tên nhân viên");
        headerRow.createCell(1).setCellValue("Mã nhân viên");
        headerRow.createCell(2).setCellValue("Ngày");
        if(departmentId != 1) {
            for(int i = 0; i < day; i++) {
                headerRow.createCell(i + 3).setCellValue(officerLog.get(i).getDay().toString());
            }
        }
        else {
            for(int i = 0; i < day; i++) {
                headerRow.createCell(i + 3).setCellValue(workerLog.get(i).getDay().toString());
            }
        }
        // Add more columns as needed, make a loop for day here
    }

    private void fillShift1Data(Row row, int userIndex, User user) {
        row.createCell(0).setCellValue(user.getName());
        row.createCell(1).setCellValue(user.getId());
        row.createCell(2).setCellValue("Ca 1");
        for(int i = 0; i < day; i++) {
            row.createCell(i + 3).setCellValue(workerLog.get(userIndex * day + i).getShift1());
        }
    }

    private void fillShift2Data(Row row, int userIndex) {
        row.createCell(0).setCellValue("");
        row.createCell(1).setCellValue("");
        row.createCell(2).setCellValue("Ca 2");
        for(int i = 0; i < day; i++) {
            row.createCell(i + 3).setCellValue(workerLog.get(userIndex * day + i).getShift2());
        }
    }

    private void fillShift3Data(Row row, int userIndex) {
        row.createCell(0).setCellValue("");
        row.createCell(1).setCellValue("");
        row.createCell(2).setCellValue("Ca 3");
        for(int i = 0; i < day; i++) {
            row.createCell(i + 3).setCellValue(workerLog.get(userIndex * day + i).getShift3());
        }
    }

    private void fillMorningData(Row row, int userIndex, User user) {
        row.createCell(0).setCellValue(user.getName());
        row.createCell(1).setCellValue(user.getId());
        row.createCell(2).setCellValue("Sáng");
        for(int i = 0; i < day; i++) {
            row.createCell(i + 3).setCellValue(officerLog.get(userIndex * day + i).isMorning());
        }
    }

    private void fillAfternoonData(Row row, int userIndex) {
        row.createCell(0).setCellValue("");
        row.createCell(1).setCellValue("");
        row.createCell(2).setCellValue("Chiều");
        for(int i = 0; i < day; i++) {
            row.createCell(i + 3).setCellValue(officerLog.get(userIndex * day + i).isAfternoon());
        }
    }

    private void fillLateData(Row row, int userIndex) {
        row.createCell(0).setCellValue("");
        row.createCell(1).setCellValue("");
        row.createCell(2).setCellValue("Đi muộn");
        for(int i = 0; i < day; i++) {
            row.createCell(i + 3).setCellValue(officerLog.get(userIndex * day + i).getTimeLate());
        }
    }

    private void fillEarlyData(Row row, int userIndex) {
        row.createCell(0).setCellValue("");
        row.createCell(1).setCellValue("");
        row.createCell(2).setCellValue("Về sớm");
        for(int i = 0; i < day; i++) {
            row.createCell(i + 3).setCellValue(officerLog.get(userIndex * day + i).getTimeEarly());
        }
    }

}
