package com.example.demo.qlns;

import com.example.demo.entity.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static com.example.demo.leaddepartment.CheckinDepartmentView.getIntegerObservableValue;

public class DepartmentDataView {

    DepartmentDataController departmentDataController = new DepartmentDataController();
    private int currentPage = 0;
    private int numPages;
    private List<OfficerLog> officerData;
    private List<OfficerLog> filterOfficerData;
    private List<User> userList;
    private List<WorkerLog> workerData;
    private List<WorkerLog> filterWorkerData;
    private List<Department> departmentList;
    private int currentDepartmentId;
    private int dayCount;
    private int dayIndex = 0;
    private int maxDayIndex;
    private int currentMonth = (new Date().getMonth()) + 1;
    private String currentPartialName = "";

    @FXML
    private TableView<Log> dataTable;

    @FXML
    private TableColumn<String, Integer> colId;

    @FXML
    private TableColumn<String, String> colName;

    @FXML
    private TableColumn<String, String> colDay;

    @FXML
    private TableColumn<String, String> colDay1;

    @FXML
    private TableColumn<String, String> colDay2;

    @FXML
    private TableColumn<String, String> colDay3;

    @FXML
    private TableColumn<String, String> colDay4;

    @FXML
    private TableColumn<String, String> colDay5;

    @FXML
    private SplitMenuButton splitMenuButton1;

    @FXML
    private SplitMenuButton splitMenuMonth;

    @FXML
    private Button leftButton;

    @FXML
    private Button rightButton;

    @FXML
    private Pagination pagination;

    @FXML
    private TextField searchField;

    @FXML
    private void initialize() throws SQLException {
        departmentList = departmentDataController.getDepartmentList();
        initializeDepartmentOption();
        leftButton.setDisable(true);
        if(currentDepartmentId != 1) {
            getOfficerLogData(currentDepartmentId);
        }
        else {
            getWorkerLogData(currentDepartmentId);
        }
        initialMonthOption();
        setPage();
        initializePagination();
        initializeTable(currentPage * 2 * dayCount, currentPage * 2 * dayCount + dayCount);

    }

    private void getOfficerLogData(int departmentId) throws SQLException {
        officerData = departmentDataController.getOfficerLogData(departmentId);
        userList = departmentDataController.getUserListByDepartmentId(departmentId);
        filterOfficerData = departmentDataController.filterOfficerData(officerData, userList, "", currentMonth);
        dayCount = departmentDataController.countDay(filterOfficerData);
    }

    private void getWorkerLogData(int departmentId) throws SQLException {
        workerData = departmentDataController.getWorkerLogData();
        userList = departmentDataController.getUserListByDepartmentId(departmentId);
        filterWorkerData = departmentDataController.filterWorkerData(workerData, userList, "", currentMonth);
        dayCount = departmentDataController.countDay(filterWorkerData);
    }

    private void setPage() {
        if(currentDepartmentId != 1) {
            numPages = (int) Math.ceil((double)filterOfficerData.size() / (2 * dayCount));
        }
        else {
            numPages = (int) Math.ceil((double)filterWorkerData.size() / (2 * dayCount));
        }
        pagination.setPageCount(numPages);
    }

    private void initializePagination() {
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            currentPage = newValue.intValue();
            initializeTable(currentPage * 2 * dayCount + dayIndex*5, currentPage * 2 * dayCount + dayCount + dayIndex*5);
        });
    }

    private void initialMonthOption() {
        splitMenuMonth.getItems().clear();
        for (int i = 1; i <= 12; i++) {
            final int month = i; // Create a final variable
            MenuItem menuItem = new MenuItem("Tháng " + month);
            menuItem.setOnAction(event -> handleMonthChoose(month));
            splitMenuMonth.getItems().add(menuItem);
        }
        handleMonthChoose(currentMonth);
    }

    private void handleMonthChoose(int month) {
        splitMenuMonth.setText("Tháng " + month);
        currentMonth = month;
        if(currentDepartmentId != 1) {
            filterOfficerData = departmentDataController.filterOfficerData(officerData, userList, currentPartialName, currentMonth);
            dayCount = departmentDataController.countDay(filterOfficerData);
        }
        else {
            filterWorkerData = departmentDataController.filterWorkerData(workerData, userList, currentPartialName, currentMonth);
            dayCount = departmentDataController.countDay(filterWorkerData);
        }
        maxDayIndex = (int) Math.ceil((double) dayCount / 5);
        leftButton.setDisable(true);
        rightButton.setDisable(maxDayIndex == 1);
        setPage();
        initializeTable(currentPage * 2 * dayCount, currentPage * 2 * dayCount + dayCount);
    }

    private void initializeDepartmentOption() throws SQLException {
        splitMenuButton1.getItems().clear();

        for (Department department : departmentList) {
            MenuItem menuItem = new MenuItem(department.getName());
            menuItem.setOnAction(event -> {
                try {
                    handleMenuItemClick(department);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            splitMenuButton1.getItems().add(menuItem);
        }

        if (!departmentList.isEmpty()) {
            handleMenuItemClick(departmentList.get(0));
            currentDepartmentId = departmentList.get(0).getId();
        }
    }

    private void handleMenuItemClick(Department selectedOption) throws SQLException {
        splitMenuButton1.setText(selectedOption.getName());
        currentDepartmentId = selectedOption.getId();
        if(currentDepartmentId != 1) {
            getOfficerLogData(currentDepartmentId);
        }
        else {
            getWorkerLogData(currentDepartmentId);
        }
        maxDayIndex = (int) Math.ceil((double) dayCount / 5);
        leftButton.setDisable(true);
        rightButton.setDisable(maxDayIndex == 1);
        setPage();
        dayIndex = 0;
        initializeTable(currentPage * 2 * dayCount, currentPage * 2 * dayCount + dayCount);
    }

    private void initializeTable(int pos1, int pos2) {
        dataTable.getItems().clear();
        if(currentDepartmentId != 1) {
            initializeTableOfficer(pos1, pos2);
        }
        else {
            initializeTableWorker(pos1, pos2);
        }
    }

    private void initializeTableWorker(int pos1, int pos2) {

        if(filterWorkerData.isEmpty() || filterWorkerData == null) return;

        for(WorkerLog data : filterWorkerData) {
            System.out.println(data.toString());
        }

        colId.setCellValueFactory(cellData -> {
            int rowIndex = cellData.getTableView().getItems().indexOf(cellData.getValue());


            if (rowIndex == 0) {
                return new SimpleObjectProperty<>(filterWorkerData.get(pos1).getId());
            } else if (rowIndex == 3 && !(currentPage == numPages && (filterWorkerData.size() / dayCount) % 2 != 0)) {
                return new SimpleObjectProperty<>(filterWorkerData.get(pos2).getId());
            } else {
                return new SimpleObjectProperty<>();
            }
        });

        colName.setCellValueFactory(cellData -> {
            int rowIndex = cellData.getTableView().getItems().indexOf(cellData.getValue());

            if (rowIndex == 0) {
                return new SimpleStringProperty(findUserById(filterWorkerData.get(pos1).getId()).getName());
            } else if (rowIndex == 3 && !(currentPage == numPages && (filterWorkerData.size() / dayCount) % 2 != 0)) {
                return new SimpleStringProperty(findUserById(filterWorkerData.get(pos2).getId()).getName());

            } else {
                return new SimpleStringProperty("");
            }
        });

        colDay.setCellValueFactory(cellData -> {
            int rowIndex = cellData.getTableView().getItems().indexOf(cellData.getValue());

            String[] values = {"Ca 1", "Ca 2", "Ca 3"};

            String dayValue = values[rowIndex % values.length];

            return new SimpleStringProperty(dayValue);
        });

        columnDataWorker(colDay1, pos1, pos2);
        if(maxDayIndex == 1) {
            if(dayCount % 5 == 0) {
                columnDataWorker(colDay2, pos1 + 1, pos2 + 1);
                columnDataWorker(colDay3, pos1 + 2, pos2 + 2);
                columnDataWorker(colDay4, pos1 + 3, pos2 + 3);
                columnDataWorker(colDay5, pos1 + 4, pos2 + 4);
            }
            else {
                if(!(dayCount % 5 <= 1)) columnDataWorker(colDay2, pos1 + 1, pos2 + 1); else emptyColumn(colDay2);
                if(!(dayCount % 5 <= 2)) columnDataWorker(colDay3, pos1 + 1, pos2 + 1); else emptyColumn(colDay3);
                if(!(dayCount % 5 <= 3)) columnDataWorker(colDay4, pos1 + 1, pos2 + 1); else emptyColumn(colDay4);
                emptyColumn(colDay5);
            }
        }
        else {
            if(!(dayIndex == maxDayIndex - 1 && dayCount % 5 <= 1)) columnDataWorker(colDay2, pos1 + 1, pos2 + 1); else emptyColumn(colDay2);
            if(!(dayIndex == maxDayIndex - 1 && dayCount % 5 <= 2)) columnDataWorker(colDay3, pos1 + 2, pos2 + 2); else emptyColumn(colDay3);
            if(!(dayIndex == maxDayIndex - 1 && dayCount % 5 <= 3)) columnDataWorker(colDay4, pos1 + 3, pos2 + 3); else emptyColumn(colDay4);
            if(!(dayIndex == maxDayIndex - 1 && dayCount % 5 != 0)) columnDataWorker(colDay5, pos1 + 4, pos2 + 4); else emptyColumn(colDay5);
        }

        if(currentPage + 1 == numPages && (filterWorkerData.size() / dayCount) % 2 != 0) {
            dataTable.getItems().addAll(filterWorkerData.subList(0, 3));
        }
        else dataTable.getItems().addAll(filterWorkerData.subList(0, Math.min(filterWorkerData.size(), 6)));
    }

    private void initializeTableOfficer(int pos1, int pos2) {

        if(filterOfficerData.isEmpty() || filterOfficerData == null) return;

        colId.setCellValueFactory(cellData -> {
            int rowIndex = cellData.getTableView().getItems().indexOf(cellData.getValue());


            return getIntegerObservableValue(pos1, pos2, rowIndex, filterOfficerData, currentPage, numPages, dayCount);
        });

        colName.setCellValueFactory(cellData -> {
            int rowIndex = cellData.getTableView().getItems().indexOf(cellData.getValue());

            if (rowIndex == 0) {
                return new SimpleStringProperty(findUserById(filterOfficerData.get(pos1).getId()).getName());

            } else if (rowIndex == 4 && !(currentPage == numPages && (filterOfficerData.size() / dayCount) % 2 != 0)) {
                return new SimpleStringProperty(findUserById(filterOfficerData.get(pos2).getId()).getName());

            } else {
                return new SimpleStringProperty("");
            }
        });

        colDay.setCellValueFactory(cellData -> {
            int rowIndex = cellData.getTableView().getItems().indexOf(cellData.getValue());

            String[] values = {"Sáng", "Chiều", "Đi muộn", "Về sớm"};

            String dayValue = values[rowIndex % values.length];

            return new SimpleStringProperty(dayValue);
        });

        columnData(colDay1, pos1, pos2);
        if(maxDayIndex == 1) {
            if(dayCount % 5 == 0) {
                columnData(colDay2, pos1 + 1, pos2 + 1);
                columnData(colDay3, pos1 + 2, pos2 + 2);
                columnData(colDay4, pos1 + 3, pos2 + 3);
                columnData(colDay5, pos1 + 4, pos2 + 4);
            }
            else {
                if(!(dayCount % 5 <= 1)) columnData(colDay2, pos1 + 1, pos2 + 1); else emptyColumn(colDay2);
                if(!(dayCount % 5 <= 2)) columnData(colDay3, pos1 + 1, pos2 + 1); else emptyColumn(colDay3);
                if(!(dayCount % 5 <= 3)) columnData(colDay4, pos1 + 1, pos2 + 1); else emptyColumn(colDay4);
                emptyColumn(colDay5);
            }
        }
        else {
            if(!(dayIndex == maxDayIndex - 1 && dayCount % 5 <= 1)) columnData(colDay2, pos1 + 1, pos2 + 1); else emptyColumn(colDay2);
            if(!(dayIndex == maxDayIndex - 1 && dayCount % 5 <= 2)) columnData(colDay3, pos1 + 2, pos2 + 2); else emptyColumn(colDay3);
            if(!(dayIndex == maxDayIndex - 1 && dayCount % 5 <= 3)) columnData(colDay4, pos1 + 3, pos2 + 3); else emptyColumn(colDay4);
            if(!(dayIndex == maxDayIndex - 1 && dayCount % 5 != 0)) columnData(colDay5, pos1 + 4, pos2 + 4); else emptyColumn(colDay5);
        }

        if(currentPage + 1 == numPages && (filterOfficerData.size() / dayCount) % 2 != 0) {
            dataTable.getItems().addAll(filterOfficerData.subList(0, 4));
        }
        else dataTable.getItems().addAll(filterOfficerData.subList(0, Math.min(filterOfficerData.size(), 8)));
    }

    // Could change to the one in Interface, but It's too much now
    private User findUserById(int userId) {
        for (User user : userList) {
            if (user.getId() == userId) {
                return user;
            }
        }
        return new User(0, "Wrong");
    }

    private void columnDataWorker(TableColumn<String, String> col, int pos1, int pos2) {
        col.setText(filterWorkerData.get(pos1).getDay().toString());
        col.setCellValueFactory(cellData -> {
            int rowIndex = cellData.getTableView().getItems().indexOf(cellData.getValue());
            return switch (rowIndex) {
                case 0 -> new SimpleDoubleProperty(filterWorkerData.get(pos1).getShift1()).asString();
                case 1 -> new SimpleDoubleProperty(filterWorkerData.get(pos1).getShift2()).asString();
                case 2 -> new SimpleDoubleProperty(filterWorkerData.get(pos1).getShift3()).asString();
                case 3 -> {
                    if (currentPage + 1 == numPages && (filterWorkerData.size() / dayCount) % 2 != 0) {
                        yield new SimpleStringProperty("");
                    }
                    yield new SimpleDoubleProperty(filterWorkerData.get(pos2).getShift1()).asString();
                }
                case 4 -> {
                    if (currentPage + 1 == numPages && (filterWorkerData.size() / dayCount) % 2 != 0) {
                        yield new SimpleStringProperty("");
                    }
                    yield new SimpleDoubleProperty(filterWorkerData.get(pos2).getShift2()).asString();
                }
                case 5 -> {
                    if (currentPage + 1 == numPages && (filterWorkerData.size() / dayCount) % 2 != 0) {
                        yield new SimpleStringProperty("");
                    }
                    yield new SimpleDoubleProperty(filterWorkerData.get(pos2).getShift3()).asString();
                }
                default -> new SimpleStringProperty("");
            };
        });
    }

    private void columnData(TableColumn<String, String> col, int pos1, int pos2) {
        col.setText(filterOfficerData.get(pos1).getDay().toString());
        col.setCellValueFactory(cellData -> {
            int rowIndex = cellData.getTableView().getItems().indexOf(cellData.getValue());
            return switch (rowIndex) {
                case 0 -> new SimpleBooleanProperty(filterOfficerData.get(pos1).isMorning()).asString();
                case 1 -> new SimpleBooleanProperty(filterOfficerData.get(pos1).isAfternoon()).asString();
                case 2 -> new SimpleDoubleProperty(filterOfficerData.get(pos1).getTimeLate()).asString();
                case 3 -> new SimpleDoubleProperty(filterOfficerData.get(pos1).getTimeEarly()).asString();
                case 4 -> {
                    if (currentPage + 1 == numPages && (filterOfficerData.size() / dayCount) % 2 != 0) {
                        yield new SimpleStringProperty("");
                    }
                    yield new SimpleBooleanProperty(filterOfficerData.get(pos2).isMorning()).asString();
                }
                case 5 -> {
                    if (currentPage + 1 == numPages && (filterOfficerData.size() / dayCount) % 2 != 0) {
                        yield new SimpleStringProperty("");
                    }
                    yield new SimpleBooleanProperty(filterOfficerData.get(pos2).isAfternoon()).asString();
                }
                case 6 -> {
                    if (currentPage + 1 == numPages && (filterOfficerData.size() / dayCount) % 2 != 0) {
                        yield new SimpleStringProperty("");
                    }
                    yield new SimpleDoubleProperty(filterOfficerData.get(pos2).getTimeLate()).asString();
                }
                case 7 -> {
                    if (currentPage + 1 == numPages && (filterOfficerData.size() / dayCount) % 2 != 0) {
                        yield new SimpleStringProperty("");
                    }
                    yield new SimpleDoubleProperty(filterOfficerData.get(pos2).getTimeEarly()).asString();
                }
                default -> new SimpleStringProperty("");
            };
        });
    }

    private void emptyColumn(TableColumn<String, String> col) {
        col.setText("");
        col.setCellValueFactory(cellData -> new SimpleStringProperty(""));
    }

    @FXML
    private void handleLeftButtonClick() {
        dayIndex--;
        initializeTable(currentPage * 2 * dayCount + dayIndex*5, currentPage * 2 * dayCount + dayCount + dayIndex*5);
        updateButtonStates();
    }

    @FXML
    private void handleRightButtonClick() {
        dayIndex++;
        initializeTable(currentPage * 2 * dayCount + dayIndex*5, currentPage * 2 * dayCount + dayCount + dayIndex*5);
        updateButtonStates();
    }

    private void updateButtonStates() {
        leftButton.setDisable(dayIndex == 0);
        rightButton.setDisable(dayIndex == (maxDayIndex - 1));
    }

    @FXML
    private void handleFilter() {
        currentPartialName = searchField.getText();
        if(currentDepartmentId != 1) {
            filterOfficerData = departmentDataController.filterOfficerData(officerData, userList, currentPartialName, currentMonth);
        }
        else {
            filterWorkerData = departmentDataController.filterWorkerData(workerData, userList, currentPartialName, currentMonth);
        }
        for(WorkerLog test : filterWorkerData) {
            System.out.println(test.toString());
        }
        setPage();
        initializeTable(currentPage * 2 * dayCount + dayIndex*5, currentPage * 2 * dayCount + dayCount + dayIndex*5);
    }

    @FXML
    private void handleExportButtonClick() {
        if(currentDepartmentId != 1) {
            filterOfficerData = departmentDataController.filterOfficerData(officerData, userList, "", currentMonth);
        }
        else {
            filterWorkerData = departmentDataController.filterWorkerData(workerData, userList, currentPartialName, currentMonth);
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/qlns/ExportScene.fxml"));
            ExportView exportViewController = new ExportView();
            ExportController exportController = new ExportController(userList, filterOfficerData, filterWorkerData, currentMonth, currentDepartmentId, dayCount);
            exportViewController.setController(exportController);

            loader.setController(exportViewController);
            Parent root = loader.load();

            Stage exportStage = new Stage();
            exportStage.setTitle("Xuất báo cáo");
            exportStage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(root);
            exportStage.setScene(scene);

            exportStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
