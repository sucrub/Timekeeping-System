package com.example.demo.qlns;

import com.example.demo.entity.OfficerLog;
import com.example.demo.entity.User;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExportView {

    List<User> user;
    ExportController exportController;
    ObservableMap<User, SimpleBooleanProperty> selectedMap;

    private int fileTypeId;

    @FXML
    private TableView<User> tableUser;

    @FXML
    private TableColumn<User, Boolean> checkbox;

    @FXML
    private TableColumn<User, Integer> userId;

    @FXML
    private TableColumn<User, String> userName;

    @FXML
    private Button exportButton;

    @FXML
    private CheckBox checkAll;

    @FXML
    private SplitMenuButton fileType;

    @FXML
    private Label month;
    @FXML
    private Label departmentName;

    @FXML
    private void initialize() throws SQLException {
        setUpPageName();
        setUpFileTypeOption();
        handleUserData();
        tableUser.setEditable(true);
        tableUser.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        initializeTable();
    }

    private void setUpPageName() throws SQLException {
        month.setText("Tháng " + exportController.getCurrentMonth());
        departmentName.setText(exportController.getDepartmentName());
    }

    private void setUpFileTypeOption() {
        MenuItem excel = new MenuItem("Excel");
        excel.setOnAction(event -> handleType(1));
        fileType.getItems().add(excel);
        MenuItem csv = new MenuItem("CSV");
        csv.setOnAction(event -> handleType(2));
        fileType.getItems().add(csv);
        handleType(1);
    }

    private void handleType(int type) {
        if(type == 1) {
            fileType.setText("Excel");
            fileTypeId = 1;
        }
        else if (type == 2) {
            fileType.setText("CSV");
            fileTypeId = 2;
        }
    }

    public ExportView() {
    }

    private void handleUserData() {
        user = exportController.getUserList();
        selectedMap = FXCollections.observableHashMap();
        for (User newUser : user) {
            selectedMap.put(newUser, new SimpleBooleanProperty(false));
        }
    }

    public void initializeTable() {
        // Configure the checkbox column
        checkbox.setCellFactory(CheckBoxTableCell.forTableColumn(checkbox));
        checkbox.setCellValueFactory(cellData -> selectedMap.get(cellData.getValue()));

        // Configure the userId column
        userId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());

        // Configure the userName column
        userName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        // Populate the table with data
        tableUser.getItems().addAll(user);

        checkAll.setOnAction(event -> handleCheckAll());
    }

    private void handleCheckAll() {
        boolean checkAllSelected = checkAll.isSelected();

        // Set the selected state for all users
        for (Map.Entry<User, SimpleBooleanProperty> entry : selectedMap.entrySet()) {
            entry.getValue().set(checkAllSelected);
        }

        // Refresh the table to reflect the changes
        tableUser.refresh();
    }

    @FXML
    private void handleExportButton(ActionEvent event) {
        System.out.println("Export Button Clicked");

        // Check if at least one user is selected
        boolean atLeastOneSelected = selectedMap.values().stream().anyMatch(SimpleBooleanProperty::get);

        if (!atLeastOneSelected) {
            // Show an alert if no user is selected
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select at least one user to export.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        // Print and export selected users
        List<User> selectedUsers = new ArrayList<>();
        for (Map.Entry<User, SimpleBooleanProperty> entry : selectedMap.entrySet()) {
            User user = entry.getKey();
            boolean isSelected = entry.getValue().get();
            if (isSelected) {
                System.out.println("User ID: " + user.getId() + " - Selected: " + true);
                selectedUsers.add(user);
            }
        }

        // Call the export method only if at least one user is selected
        exportController.exportData(selectedUsers, fileTypeId);
    }

    public void setController(ExportController exportController) {
        this.exportController = exportController;
    }
}
