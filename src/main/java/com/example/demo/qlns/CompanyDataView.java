package com.example.demo.qlns;

import com.example.demo.entity.Department;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CompanyDataView {

    @FXML private SplitMenuButton typeSelect;
    @FXML private SplitMenuButton yearSelect;
    @FXML private SplitMenuButton timeSelect;
    @FXML private SplitMenuButton departmentSelect;
    @FXML private Label departmentLabel;
    @FXML private Label type1Label;
    @FXML private Label type2Label;
    @FXML private Label type3Label;
    @FXML private Label type4Label;
    @FXML private Label type5Label;
    @FXML private Label data1Label;
    @FXML private Label data2Label;
    @FXML private Label data3Label;
    @FXML private Label data4Label;
    @FXML private Label data5Label;

    private int currentDepartmentId = 0;
    private int currentYear = 2023;
    private int currentOption = 1; // 1 2 3 All Quad Month
    private int currentQuad = 1;
    private int currentMonth = 1; // Just keep it like this, its overall data anyway

    private final CompanyDataController companyDataController = new CompanyDataController();
    private final ImportControl ImportControl = new ImportControl();
    public void initialize() throws SQLException {
        setUpDepartmentOption();
        setUpYearOption();
        setUpTypeOption();
    }

    private void setUpDepartmentOption() throws SQLException {
        List<Department> departmentList = companyDataController.getDepartmentList();
        MenuItem allOption = new MenuItem("All");
        allOption.setOnAction(event -> {
            try {
                handleAllDepartmentOption();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        departmentSelect.getItems().add(allOption);
        for(Department data : departmentList) {
            MenuItem departmentOption = new MenuItem(data.getName());
            departmentOption.setOnAction(event -> {
                try {
                    handleDepartmentOption(data.getName(), data.getId());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            departmentSelect.getItems().add(departmentOption);
        }
        handleAllDepartmentOption();
    }

    private void handleAllDepartmentOption() throws SQLException {
        currentDepartmentId = 0;
        departmentSelect.setText("All");
        departmentLabel.setText("All");
        handleData(currentDepartmentId, currentYear, currentOption, currentQuad, currentMonth);
    }

    private void handleDepartmentOption(String departmentName, int departmentId) throws SQLException {
        departmentSelect.setText(departmentName);
        currentDepartmentId = departmentId;
        departmentLabel.setText(departmentName);
        handleData(currentDepartmentId, currentYear, currentOption, currentQuad, currentMonth);
    }

    private void setUpYearOption() throws SQLException {
        for(int i = 2020; i <= 2023; i++) {
            MenuItem yearOption = new MenuItem("Năm " + i);
            int year = i;
            yearOption.setOnAction(event -> {
                try {
                    handleYearOption(year);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            yearSelect.getItems().add(yearOption);
        }
        handleYearOption(2023);
    }

    private void handleYearOption(int year) throws SQLException {
        System.out.println("Year: " + year);
        currentYear = year;
        yearSelect.setText("Năm " + year);
        handleData(currentDepartmentId, currentYear, currentOption, currentQuad, currentMonth);
    }

    private void setUpTypeOption() throws SQLException {
        MenuItem type1 = new MenuItem("All");
        type1.setOnAction(event -> {
            try {
                handleAllOption();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        typeSelect.getItems().add(type1);
        MenuItem type2 = new MenuItem("Quý");
        type2.setOnAction(event -> {
            try {
                handleQuadOption();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        typeSelect.getItems().add(type2);
        MenuItem type3 = new MenuItem("Tháng");
        type3.setOnAction(event -> {
            try {
                handleMonthOption();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        typeSelect.getItems().add(type3);
        handleAllOption();
    }

    private void handleAllOption() throws SQLException {
        typeSelect.setText("All");
        System.out.println("Select all");
        timeSelect.setText("All");
        currentOption = 1;
        timeSelect.getItems().clear();
        handleData(currentDepartmentId, currentYear, currentOption, currentQuad, currentMonth);
    }

    private void handleQuadOption() throws SQLException {
        typeSelect.setText("Quý");
        System.out.println("Select quý");
        currentOption = 2;
        timeSelect.getItems().clear();
        setUpQuadOption();
        handleData(currentDepartmentId, currentYear, currentOption, currentQuad, currentMonth);
    }

    private void setUpQuadOption() throws SQLException {
        for(int i = 1; i <= 4; i++) {
            MenuItem quadItem = new MenuItem("Quý " + i);
            int quad = i;
            quadItem.setOnAction(event -> {
                try {
                    handlePickQuadOption(quad);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            timeSelect.getItems().add(quadItem);
        }
        handlePickQuadOption(1);
    }

    private void handlePickQuadOption(int quad) throws SQLException {
        timeSelect.setText("Quý " + quad);
        currentQuad = quad;
        System.out.println("Quad selected: " + quad);
        handleData(currentDepartmentId, currentYear, currentOption, currentQuad, currentMonth);
    }

    private void handleMonthOption() throws SQLException {
        typeSelect.setText("Tháng");
        System.out.println("Select tháng");
        currentOption = 3;
        timeSelect.getItems().clear();
        setUpMonthOption();
        handleData(currentDepartmentId, currentYear, currentOption, currentQuad, currentMonth);
    }

    private void setUpMonthOption() throws SQLException {
        for(int i = 1; i <= 12; i++) {
            MenuItem monthItem = new MenuItem("Tháng " + i);
            int month = i;
            monthItem.setOnAction(event -> {
                try {
                    handlePickMonthOption(month);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            timeSelect.getItems().add(monthItem);
        }
        handlePickMonthOption(1);
    }

    private void handlePickMonthOption(int month) throws SQLException {
        timeSelect.setText("Tháng " + month);
        currentMonth = month;
        System.out.println("Month selected: " + month);
        handleData(currentDepartmentId, currentYear, currentOption, currentQuad, currentMonth);
    }

    public void handleData(int departmentId, int year, int option, int quad, int month) throws SQLException {
        double shift1 = companyDataController.getShift1(year, departmentId, option, month, quad);
        double shift2 = companyDataController.getShift2(year, departmentId, option, month, quad);
        double shift3 = companyDataController.getShift3(year, departmentId, option, month, quad);
        double timeLate = companyDataController.getTimeLate(year, departmentId, option, month, quad);
        double timeEarly = companyDataController.getTimeEarly(year, departmentId, option, month, quad);
        if(departmentId == 0) {
            handleScreenAll(shift1, shift2, shift3, timeLate, timeEarly);
        }
        else if(departmentId == 1) {
            handleScreenWorker(shift1, shift2, shift3);
        }
        else {
            handleScreenOfficer(timeLate, timeEarly);
        }
    }

    private void handleScreenAll(double shift1, double shift2, double shift3, double timeLate, double timeEarly) {
        type1Label.setText("Ca 1: ");
        type2Label.setText("Ca 2: ");
        type3Label.setText("Ca 3: ");
        type4Label.setText("Đi muộn: ");
        type5Label.setText("Về sớm: ");
        data1Label.setText(String.format("%.2f", shift1));
        data2Label.setText(String.format("%.2f", shift2));
        data3Label.setText(String.format("%.2f", shift3));
        data4Label.setText(String.format("%.2f", timeLate));
        data5Label.setText(String.format("%.2f", timeEarly));
    }

    private void handleScreenWorker(double shift1, double shift2, double shift3) {
        type1Label.setText("Ca 1: ");
        type2Label.setText("Ca 2: ");
        type3Label.setText("Ca 3: ");
        type4Label.setText("");
        type5Label.setText("");
        data1Label.setText(String.format("%.2f", shift1));
        data2Label.setText(String.format("%.2f", shift2));
        data3Label.setText(String.format("%.2f", shift3));
        data4Label.setText("");
        data5Label.setText("");
    }

    private void handleScreenOfficer(double timeLate, double timeEarly) {
        type1Label.setText("Đi muộn: ");
        type2Label.setText("Về sớm: ");
        type3Label.setText("");
        type4Label.setText("");
        type5Label.setText("");
        data1Label.setText(String.format("%.2f", timeLate));
        data2Label.setText(String.format("%.2f", timeEarly));
        data3Label.setText("");
        data4Label.setText("");
        data5Label.setText("");
    }

    @FXML
    private void handleImport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            try {
                ImportControl.ImportLogs(selectedFile);
                setUpDepartmentOption();
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // for testing
    public CompanyDataController getController() {
        return companyDataController;
    }
}
