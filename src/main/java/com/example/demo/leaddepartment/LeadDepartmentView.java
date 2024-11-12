package com.example.demo.leaddepartment;

import com.example.demo.entity.User;
import com.example.demo.userprofile.ProfileController;
import com.example.demo.userprofile.ProfileView;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Optional;

public class LeadDepartmentView {
    private int departmentId;
    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
    @FXML
    private StackPane contentPane;
    @FXML
    private Label userName;
    @FXML
    private Label sceneLabel;

    @FXML
    private Button option1Button;
    @FXML
    private Button logoutButton;

    @FXML
    private Button option2Button;

    @FXML
    private Button option3Button;
    @FXML
    private Button option4Button;

    private User loginUser;
    private final BooleanProperty option1Selected = new SimpleBooleanProperty(false);
    private final BooleanProperty option2Selected = new SimpleBooleanProperty(false);
    private final BooleanProperty option3Selected = new SimpleBooleanProperty(false);
    private final BooleanProperty option4Selected = new SimpleBooleanProperty(false);

    @FXML
    public void initialize() {
        initializeButtonStyles();
    }

    @FXML
    private void handleSidebarOptionClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String optionId = clickedButton.getId();

        switch (optionId) {
            case "option1":
                loadOption1Scene(loginUser);
                break;
            case "option2":
                loadOption2Scene();
                break;
            case "option3":
                onClickReport();
                break;
            case "option4":
                loadOption4Scene();
                break;
            default:
                showAlert();
        }
    }

    private void loadOption1Scene(User loginUser) {
        try {
            sceneLabel.setText("Trang chủ");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/demo/userprofile/ProfileScene.fxml"));
            ProfileView profileView = new ProfileView();
            ProfileController profileController = new ProfileController(loginUser);
            profileView.setController(profileController);
            loader.setController(profileView);
            Parent root = loader.load();

            // Set the loaded content as the right part of the contentPane
            contentPane.getChildren().clear();
            contentPane.getChildren().add(root);

            // Set the selected property for option1
            option1Selected.set(true);
            option2Selected.set(false);
            option3Selected.set(false);
            option4Selected.set(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void onClickReport() {
        try {
            if (loginUser.getDepartmentId() == 1) {
                sceneLabel.setText("Xem báo cáo công nhân");
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/com/example/demo/leaddepartment/WorkerReportScene.fxml"));
                Parent root = loader.load();

                // Set the loaded content as the right part of the contentPane
                contentPane.getChildren().clear();
                contentPane.getChildren().add(root);

                // Set the selected property for option2
            } else {
                sceneLabel.setText("Xem báo cáo nhân viên");
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/com/example/demo/leaddepartment/OfficerReportScene.fxml"));
                Parent root = loader.load();

                OfficerReportView ctrl = loader.getController();
                ctrl.setDepartmentId(loginUser.getDepartmentId());

                // Set the loaded content as the right part of the contentPane
                contentPane.getChildren().clear();
                contentPane.getChildren().add(root);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        option1Selected.set(false);
        option2Selected.set(false);
        option3Selected.set(true);
        option4Selected.set(false);
    }

    private void loadOption2Scene() {
        try {
            sceneLabel.setText("Xem thông tin chấm công");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/demo/leaddepartment/ShowDepartmentScene.fxml"));
            Parent root = loader.load();
            CheckinDepartmentView ctrl = loader.getController();
            ctrl.setDepartmentId(departmentId);

            // Set the loaded content as the right part of the contentPane
            contentPane.getChildren().clear();
            contentPane.getChildren().add(root);

            // Set the selected property for option2
            option1Selected.set(false);
            option2Selected.set(true);
            option3Selected.set(false);
            option4Selected.set(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void loadOption4Scene() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận");
            alert.setHeaderText(null);
            alert.setContentText("Bạn muốn đăng xuất?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get().equals(ButtonType.OK)) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/com/example/demo/login/login-view.fxml"));
                Parent root = loader.load();

                // Get the current scene
                Scene currentScene = contentPane.getScene();

                // Set the loaded content as the root of the current scene
                currentScene.setRoot(root);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setContentText("Invalid option selected.");
        alert.showAndWait();
    }

    private void initializeButtonStyles() {
        // Bind the button styles to the selected properties
        BooleanBinding option1SelectedBinding = Bindings.createBooleanBinding(
                option1Selected::get,
                option1Selected
        );
        BooleanBinding option2SelectedBinding = Bindings.createBooleanBinding(
                option2Selected::get,
                option2Selected
        );
        BooleanBinding option3SelectedBinding = Bindings.createBooleanBinding(
                option3Selected::get,
                option3Selected
        );
        BooleanBinding option4SelectedBinding = Bindings.createBooleanBinding(
                option4Selected::get,
                option4Selected
        );

        option1Button.styleProperty().bind(
                Bindings.when(option1SelectedBinding)
                        .then("-fx-background-color: rgba(0, 0, 0, 0.42);")
                        .otherwise("")
        );
        option2Button.styleProperty().bind(
                Bindings.when(option2SelectedBinding)
                        .then("-fx-background-color: rgba(0, 0, 0, 0.42);")
                        .otherwise("")
        );
        option3Button.styleProperty().bind(
                Bindings.when(option3SelectedBinding)
                        .then("-fx-background-color: rgba(0, 0, 0, 0.42);")
                        .otherwise("")
        );
        option4Button.styleProperty().bind(
                Bindings.when(option4SelectedBinding)
                        .then("-fx-background-color: rgba(0, 0, 0, 0.42);")
                        .otherwise("")
        );
    }
    public User getLoginUser() {
        return loginUser;
    }
    public void setLoginUser(User loginUser) {
        this.loginUser = loginUser;
        userName.setText(loginUser.getName());
        loadOption1Scene(loginUser);
    }

    @FXML
    private void onClickLogout() {

    }
}
