package com.example.demo.leaddepartment;

import com.example.demo.entity.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.List;

public class CheckinDepartmentView {
    DepartmentCheckinController departmentCheckinController = new DepartmentCheckinController();

    private int currentPage = 0;
    private int numPages;
    private List<WorkerLog> workerData;
    private List<WorkerLog> filterWorkerData;
    private List<User> userList;
    private List<OfficerLog> officerData;
    private List<OfficerLog> filterOfficerData;
    private int departmentId;
    private int dayCount;
    private int dayIndex = 0;
    private int maxDayIndex;
    private int currentMonth;
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
        leftButton.setDisable(true);
        if(departmentId != 1) {
            getOfficerLogData(departmentId);
        } else {
            getWorkerLogData(departmentId);
        }
//        maxDayIndex = (int) Math.ceil((double) dayCount / 5);
        setPage();
        initializePagination();
        initialMonthOption();
        initializeTable(currentPage * 2 * dayCount, currentPage * 2 * dayCount + dayCount);
    }

    private void getOfficerLogData(int departmentId) throws SQLException {
        officerData = departmentCheckinController.generateOfficerData(departmentId);
        userList = departmentCheckinController.getUser(departmentId);
        filterOfficerData = departmentCheckinController.filterOfficerData(officerData, userList, "");
        dayCount = departmentCheckinController.countDay(filterOfficerData);
    }
    private void getWorkerLogData(int departmentId) throws SQLException {
        workerData = departmentCheckinController.generateWorkerData();
        userList = departmentCheckinController.getUser(departmentId);
        filterWorkerData = departmentCheckinController.filterWorkerData(workerData, userList, "");
        dayCount = departmentCheckinController.countDay(filterWorkerData);
    }
    private void setPage() {
        if(departmentId != 1) {
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
            final int month = i;
            MenuItem menuItem = new MenuItem("Tháng " + month);
            menuItem.setOnAction(event -> handleMonthChoose(month));
            splitMenuMonth.getItems().add(menuItem);
        }
        handleMonthChoose(12);
    }
    private void handleMonthChoose(int month) {
        splitMenuMonth.setText("Tháng " + month);
        currentMonth = month;
        if (departmentId != 1) {
            filterOfficerData = departmentCheckinController.filterOfficerData(officerData, userList, currentPartialName, currentMonth);
            dayCount = departmentCheckinController.countDay(filterOfficerData);
        } else {
            filterWorkerData = departmentCheckinController.filterWorkerData(workerData, userList, currentPartialName, currentMonth);
            dayCount = departmentCheckinController.countDay(filterWorkerData);
        }
        maxDayIndex = (int) Math.ceil((double) dayCount / 5);
        if(maxDayIndex == 1) rightButton.setDisable(true);
        setPage();
        initializeTable(currentPage * 2 * dayCount, currentPage * 2 * dayCount + dayCount);
    }

    private void initializeTable(int pos1, int pos2) {
        dataTable.getItems().clear();
        if(departmentId != 1) {
            initializeTableOfficer(pos1, pos2);
        } else {
            initializeTableWorker(pos1, pos2);
        }
    }
    private void initializeTableWorker(int pos1, int pos2) {

        if(filterWorkerData.isEmpty()) return;

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
            } else {
                if(!(dayCount % 5 <= 1)) columnDataWorker(colDay2, pos1 + 1, pos2 + 1); else emptyColumn(colDay2);
                if(!(dayCount % 5 <= 2)) columnDataWorker(colDay3, pos1 + 1, pos2 + 1); else emptyColumn(colDay3);
                if(!(dayCount % 5 <= 3)) columnDataWorker(colDay4, pos1 + 1, pos2 + 1); else emptyColumn(colDay4);
                emptyColumn(colDay5);
            }
        } else {
            if(!(dayIndex == maxDayIndex - 1 && dayCount % 5 <= 1)) columnDataWorker(colDay2, pos1 + 1, pos2 + 1); else emptyColumn(colDay2);
            if(!(dayIndex == maxDayIndex - 1 && dayCount % 5 <= 2)) columnDataWorker(colDay3, pos1 + 2, pos2 + 2); else emptyColumn(colDay3);
            if(!(dayIndex == maxDayIndex - 1 && dayCount % 5 <= 3)) columnDataWorker(colDay4, pos1 + 3, pos2 + 3); else emptyColumn(colDay4);
            if(!(dayIndex == maxDayIndex - 1)) columnDataWorker(colDay5, pos1 + 4, pos2 + 4); else emptyColumn(colDay5);
        }

        if(currentPage + 1 == numPages && (filterWorkerData.size() / dayCount) % 2 != 0) {
            dataTable.getItems().addAll(filterWorkerData.subList(0, 3));
        }
        else dataTable.getItems().addAll(filterWorkerData.subList(0, Math.min(filterWorkerData.size(), 6)));
    }

    private void initializeTableOfficer(int pos1, int pos2) {

        if(filterOfficerData.isEmpty()) return;

        colId.setCellValueFactory(cellData -> {
            int rowIndex = cellData.getTableView().getItems().indexOf(cellData.getValue());

            System.out.println(rowIndex);

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

        columnDataOfficer(colDay1, pos1, pos2);
        if(maxDayIndex == 1) {
            if(dayCount % 5 == 0) {
                columnDataOfficer(colDay2, pos1 + 1, pos2 + 1);
                columnDataOfficer(colDay3, pos1 + 2, pos2 + 2);
                columnDataOfficer(colDay4, pos1 + 3, pos2 + 3);
                columnDataOfficer(colDay5, pos1 + 4, pos2 + 4);
            }
            else {
                if(!(dayCount % 5 <= 1)) columnDataOfficer(colDay2, pos1 + 1, pos2 + 1); else emptyColumn(colDay2);
                if(!(dayCount % 5 <= 2)) columnDataOfficer(colDay3, pos1 + 1, pos2 + 1); else emptyColumn(colDay3);
                if(!(dayCount % 5 <= 3)) columnDataOfficer(colDay4, pos1 + 1, pos2 + 1); else emptyColumn(colDay4);
                emptyColumn(colDay5);
            }
        }
        else {
            if(!(dayIndex == maxDayIndex - 1 && dayCount % 5 <= 1)) columnDataOfficer(colDay2, pos1 + 1, pos2 + 1); else emptyColumn(colDay2);
            if(!(dayIndex == maxDayIndex - 1 && dayCount % 5 <= 2)) columnDataOfficer(colDay3, pos1 + 2, pos2 + 2); else emptyColumn(colDay3);
            if(!(dayIndex == maxDayIndex - 1 && dayCount % 5 <= 3)) columnDataOfficer(colDay4, pos1 + 3, pos2 + 3); else emptyColumn(colDay4);
            if(!(dayIndex == maxDayIndex - 1)) columnDataOfficer(colDay5, pos1 + 4, pos2 + 4); else emptyColumn(colDay5);
        }
        if(currentPage + 1 == numPages && (filterOfficerData.size() / dayCount) % 2 != 0) {
            dataTable.getItems().addAll(filterOfficerData.subList(0, 4));
        }
        else dataTable.getItems().addAll(filterOfficerData.subList(0, Math.min(filterOfficerData.size(), 8)));
    }

    public static ObservableValue<Integer> getIntegerObservableValue(int pos1, int pos2, int rowIndex, List<OfficerLog> filterOfficerData, int currentPage, int numPages, int dayCount) {
        if (rowIndex == 0) {
            return new SimpleObjectProperty<>(filterOfficerData.get(pos1).getId());
        } else if (rowIndex == 4 && !(currentPage == numPages && (filterOfficerData.size() / dayCount) % 2 != 0)) {
            return new SimpleObjectProperty<>(filterOfficerData.get(pos2).getId());
        } else {
            return new SimpleObjectProperty<>();
        }
    }

    private User findUserById(int userId) {
        for (User user : userList) {
            if (user.getId() == userId) {
                return user;
            }
        }
        return new User(0, "Không tìm thấy");
    }

    private void columnDataWorker(TableColumn<String, String> col, int pos1, int pos2) {
        col.setText(filterWorkerData.get(pos1).getDay().toString());
        col.setCellValueFactory(cellData -> {
            int rowIndex = cellData.getTableView().getItems().indexOf(cellData.getValue());
            return switch (rowIndex) {
                case 0 -> new SimpleDoubleProperty(roundTo1Decimal(filterWorkerData.get(pos1).getShift1())).asString();
                case 1 -> new SimpleDoubleProperty(roundTo1Decimal(filterWorkerData.get(pos1).getShift2())).asString();
                case 2 -> new SimpleDoubleProperty(roundTo1Decimal(filterWorkerData.get(pos1).getShift3())).asString();
                case 3 -> {
                    if (currentPage + 1 == numPages && (filterOfficerData.size() / dayCount) % 2 != 0) {
                        yield new SimpleStringProperty("");
                    }
                    yield new SimpleDoubleProperty(roundTo1Decimal(filterWorkerData.get(pos2).getShift1())).asString();
                }
                case 4 -> {
                    if (currentPage + 1 == numPages && (filterOfficerData.size() / dayCount) % 2 != 0) {
                        yield new SimpleStringProperty("");
                    }
                    yield new SimpleDoubleProperty(roundTo1Decimal(filterWorkerData.get(pos2).getShift2())).asString();
                }
                case 5 -> {
                    if (currentPage + 1 == numPages && (filterWorkerData.size() / dayCount) % 2 != 0) {
                        yield new SimpleStringProperty("");
                    }
                    yield new SimpleDoubleProperty(roundTo1Decimal(filterWorkerData.get(pos2).getShift3())).asString();
                }
                default -> new SimpleStringProperty("");
            };
        });
    }

    private void columnDataOfficer(TableColumn<String, String> col, int pos1, int pos2) {
        col.setText(filterOfficerData.get(pos1).getDay().toString());
        col.setCellValueFactory(cellData -> {
            int rowIndex = cellData.getTableView().getItems().indexOf(cellData.getValue());
            return switch (rowIndex) {
                case 0 -> new SimpleBooleanProperty(filterOfficerData.get(pos1).isMorning()).asString();
                case 1 -> new SimpleBooleanProperty(filterOfficerData.get(pos1).isAfternoon()).asString();
                case 2 ->
                        new SimpleDoubleProperty(roundTo1Decimal(filterOfficerData.get(pos1).getTimeLate())).asString();
                case 3 ->
                        new SimpleDoubleProperty(roundTo1Decimal(filterOfficerData.get(pos1).getTimeEarly())).asString();
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
                    yield new SimpleDoubleProperty(roundTo1Decimal(filterOfficerData.get(pos2).getTimeLate())).asString();
                }
                case 7 -> {
                    if (currentPage + 1 == numPages && (filterOfficerData.size() / dayCount) % 2 != 0) {
                        yield new SimpleStringProperty("");
                    }
                    yield new SimpleDoubleProperty(roundTo1Decimal(filterOfficerData.get(pos2).getTimeEarly())).asString();
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
    private void handleLeftButtonClick(ActionEvent event) {
        dayIndex--;
        initializeTable(currentPage * 2 * dayCount + dayIndex*5, currentPage * 2 * dayCount + dayCount + dayIndex*5);
        updateButtonStates();
    }

    @FXML
    private void handleRightButtonClick(ActionEvent event) {
        dayIndex++;
        initializeTable(currentPage * 2 * dayCount + dayIndex*5, currentPage * 2 * dayCount + dayCount + dayIndex*5);
        updateButtonStates();
    }

    private void updateButtonStates() {
        leftButton.setDisable(dayIndex == 0);
        rightButton.setDisable(dayIndex == (maxDayIndex - 1));
    }

    private double roundTo1Decimal(double value) {
        // Round to 1 decimal place and ensure it's positive
        return Math.max(Math.round(value * 10.0) / 10.0, 0.0);
    }
    @FXML
    private void handleFilter(ActionEvent event) {
        currentPartialName = searchField.getText();
        if(departmentId != 1) {
            filterOfficerData = departmentCheckinController.filterOfficerData(officerData, userList, currentPartialName);
        }
        else {
            filterWorkerData = departmentCheckinController.filterWorkerData(workerData, userList, currentPartialName);
        }
        setPage();
        initializeTable(currentPage * 2 * dayCount + dayIndex*5, currentPage * 2 * dayCount + dayCount + dayIndex*5);
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
}
