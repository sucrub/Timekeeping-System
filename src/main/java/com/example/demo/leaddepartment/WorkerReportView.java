package com.example.demo.leaddepartment;

import com.example.demo.entity.User;
import com.example.demo.entity.WorkerLog;
import com.example.demo.log.LogControl;
import com.example.demo.qlnssystem.QLNSSystemData;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class WorkerReportView implements Initializable {
    @FXML
    private Pagination paginate;
    @FXML
    private TableView<WorkerLog> reportTable;
    @FXML
    private TableColumn<WorkerLog, Integer> maNV;
    @FXML
    private TableColumn<WorkerLog, String> fullName;
    @FXML
    private TableColumn<WorkerLog, Double> shift1;
    @FXML
    private TableColumn<WorkerLog, Double> shift2;
    @FXML
    private TableColumn<WorkerLog, Double> shift3;
    @FXML
    private ChoiceBox choiceMonth, choiceYear;
    private List<WorkerLog> data;
    private final int rowsPerPage = 10;
    private static int dataSize = 50;
    LogControl logControl = new LogControl();
    List<User> listWorker = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        maNV.setCellValueFactory(new PropertyValueFactory<>("id"));
        fullName.setCellValueFactory(new PropertyValueFactory<>("name"));
        shift1.setCellValueFactory(new PropertyValueFactory<>("shift1"));
        shift2.setCellValueFactory(new PropertyValueFactory<>("shift2"));
        shift3.setCellValueFactory(new PropertyValueFactory<>("shift3"));

        int currYear = LocalDate.now().getYear();
        for (int i = 1; i <= 12; i++) {
            choiceMonth.getItems().add(i);
            choiceYear.getItems().add(currYear-i+1);
        }
        choiceMonth.setValue(LocalDate.now().getMonth().getValue());
        choiceYear.setValue(currYear);

        QLNSSystemData qlnsSystemData = new QLNSSystemData();
        try {
            listWorker = qlnsSystemData.getUsersByDepartment(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        data = logControl.getWorkerLogData(listWorker, ((Integer) choiceMonth.getValue()), ((Integer) choiceYear.getValue()));
        dataSize = data.size();

        paginate.setPageCount(dataSize%rowsPerPage==0 ? dataSize/rowsPerPage : dataSize/rowsPerPage+1);
        paginate.setPageFactory(this::createPage);
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, dataSize);
        reportTable.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));
        return new VBox();
    }
    @FXML
    void fetchData() {
        data = logControl.getWorkerLogData(listWorker, ((Integer) choiceMonth.getValue()), ((Integer) choiceYear.getValue()));
    }
}