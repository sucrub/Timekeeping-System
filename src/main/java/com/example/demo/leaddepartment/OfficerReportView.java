package com.example.demo.leaddepartment;

import com.example.demo.entity.OfficerLog;
import com.example.demo.entity.User;
import com.example.demo.log.LogControl;
import com.example.demo.qlnssystem.QLNSSystemData;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class OfficerReportView implements Initializable {
    @FXML
    private Pagination paginate;
    @FXML
    private TableView<OfficerLog> reportTable;
    @FXML
    private TableColumn<OfficerLog, Integer> maNV;
    @FXML
    private TableColumn<OfficerLog, String> fullName;
    @FXML
    private TableColumn<OfficerLog, Double> sumWorkSession;
    @FXML
    private TableColumn<OfficerLog, Double> sumWorkLate;
    @FXML
    private TableColumn<OfficerLog, Double> sumWorkEarly;
    @FXML
    private ChoiceBox choiceMonth, choiceYear;
    private List<OfficerLog> data;
    private final int rowsPerPage = 10;
    private static int dataSize = 50;



    private int departmentId = -1;
    LogControl logControl = new LogControl();
    List<User> listOfficer = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        maNV.setCellValueFactory(new PropertyValueFactory<OfficerLog, Integer>("id"));
        fullName.setCellValueFactory(new PropertyValueFactory<OfficerLog, String>("name"));
        sumWorkSession.setCellValueFactory(new PropertyValueFactory<OfficerLog, Double>("sumWorkSession"));
        sumWorkLate.setCellValueFactory(new PropertyValueFactory<OfficerLog, Double>("timeLate"));
        sumWorkEarly.setCellValueFactory(new PropertyValueFactory<OfficerLog, Double>("timeEarly"));

        int currYear = LocalDate.now().getYear();
        for (int i = 1; i <= 12; i++) {
            choiceMonth.getItems().add(i);
            choiceYear.getItems().add(currYear-i+1);
        }
        choiceMonth.setValue(LocalDate.now().getMonth().getValue());
        choiceYear.setValue(currYear);
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, dataSize);
        reportTable.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));
        return new VBox();
    }
    @FXML
    void fetchData() {
        data = logControl.getOfficerLogData(listOfficer, ((Integer) choiceMonth.getValue()), ((Integer) choiceYear.getValue()));
    }
    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
        QLNSSystemData qlnsSystemData = new QLNSSystemData();
        try {
            listOfficer = qlnsSystemData.getUsersByDepartment(departmentId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        data = logControl.getOfficerLogData(listOfficer, ((Integer) choiceMonth.getValue()), ((Integer) choiceYear.getValue()));
        dataSize = data.size();
        System.out.println(dataSize);
        System.out.println(departmentId);

        paginate.setPageCount(dataSize%rowsPerPage==0 ? dataSize/rowsPerPage : dataSize/rowsPerPage+1);
        paginate.setPageFactory(this::createPage);
        fetchData();
    }
}