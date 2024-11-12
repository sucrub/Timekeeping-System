package com.example.demo.qlns;

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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Optional;

public class QLNSView {

    @FXML
    private Label userName;
    @FXML
    private StackPane contentPane;
    @FXML
    private Label sceneLabel;
    @FXML
    private Button option1Button;
    @FXML
    private Button option2Button;
    @FXML
    private Button option3Button;
    @FXML
    private Button logoutButton;
    @FXML
    private Button option4Button;
    @FXML
    private Button option5Button;

    public void setLoginUser(User loginUser) {
        this.loginUser = loginUser;
        userName.setText(loginUser.getName());
        loadOption1Scene(loginUser);
    }

    private User loginUser;
    private final BooleanProperty option1Selected = new SimpleBooleanProperty(false);
    private final BooleanProperty option2Selected = new SimpleBooleanProperty(false);
    private final BooleanProperty option3Selected = new SimpleBooleanProperty(false);
    private final BooleanProperty option4Selected = new SimpleBooleanProperty(false);
    private final BooleanProperty option5Selected = new SimpleBooleanProperty(false);

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
                loadOption3Scene();
                break;
            case "option4":
                loadOption4Scene();
                break;
            case "option5":
                loadOption5Scene();
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
            option5Selected.set(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadOption2Scene() {
        try {
            sceneLabel.setText("Xem báo cáo doanh nghiệp");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/demo/qlns/CompanyDataScene.fxml"));
            Parent root = loader.load();

            // Set the loaded content as the right part of the contentPane
            contentPane.getChildren().clear();
            contentPane.getChildren().add(root);

            // Set the selected property for option2
            option1Selected.set(false);
            option2Selected.set(true);
            option3Selected.set(false);
            option4Selected.set(false);
            option5Selected.set(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadOption3Scene() {
        try {
            sceneLabel.setText("Xem báo cáo theo đơn vị");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/demo/departmentdata/DepartmentDataScene.fxml"));
            Parent root = loader.load();

            // Set the loaded content as the right part of the contentPane
            contentPane.getChildren().clear();
            contentPane.getChildren().add(root);

            // Set the selected property for option3
            option1Selected.set(false);
            option2Selected.set(false);
            option3Selected.set(true);
            option4Selected.set(false);
            option5Selected.set(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadOption4Scene() {
        try {
            sceneLabel.setText("Xử lý yêu cầu chỉnh sửa");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/demo/qlns/Option1Scene.fxml"));
            Parent root = loader.load();

            // Set the loaded content as the right part of the contentPane
            contentPane.getChildren().clear();
            contentPane.getChildren().add(root);

            // Set the selected property for option3
            option1Selected.set(false);
            option2Selected.set(false);
            option3Selected.set(false);
            option4Selected.set(true);
            option5Selected.set(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showLogoutConfirmation() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("Click OK to confirm, or Cancel to go back.");

        ButtonType buttonTypeOK = new ButtonType("OK");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonType.CANCEL.getButtonData());
        alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeOK) {
                System.out.println("Logging out...");
                loadOption5Scene();
            } else {
                System.out.println("Cancelled logout");
            }
        });
    }

    private void loadOption5Scene() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận");
            alert.setHeaderText(null);
            alert.setContentText("Bạn muốn đăng xuất?");
            Optional<ButtonType> option = alert.showAndWait();
            if (!option.get().equals(ButtonType.OK)) {
                return;
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/demo/login/login-view.fxml"));
            Parent root = loader.load();

            // Get the current scene
            Scene currentScene = contentPane.getScene();

            // Set the loaded content as the root of the current scene
            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert() {
        Alert alert = new Alert(AlertType.INFORMATION);
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
        BooleanBinding option5SelectedBinding = Bindings.createBooleanBinding(
                option5Selected::get,
                option5Selected
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
        option5Button.styleProperty().bind(
                Bindings.when(option5SelectedBinding)
                        .then("-fx-background-color: rgba(0, 0, 0, 0.42);")
                        .otherwise("")
        );
    }
}
