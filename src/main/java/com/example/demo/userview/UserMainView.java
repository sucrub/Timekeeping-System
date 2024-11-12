package com.example.demo.userview;

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

public class UserMainView {
    @FXML
    private StackPane contentPane;
    @FXML
    private Label userName;
    @FXML
    private Label sceneLabel;

    @FXML
    private Button option1Button;

    @FXML
    private Button option2Button;
    @FXML
    private Button option3Button;
    @FXML
    private Button option4Button;
    @FXML
    private Button option5Button;
    @FXML
    private Button option6Button;

    private final BooleanProperty option1Selected = new SimpleBooleanProperty(false);
    private final BooleanProperty option2Selected = new SimpleBooleanProperty(false);
    private final BooleanProperty option3Selected = new SimpleBooleanProperty(false);
    private final BooleanProperty option4Selected = new SimpleBooleanProperty(false);
    private final BooleanProperty option5Selected = new SimpleBooleanProperty(false);
    private final BooleanProperty option6Selected = new SimpleBooleanProperty(false);
    private User loginUser;
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
            case "option6":
                loadOption6Scene();
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

            contentPane.getChildren().clear();
            contentPane.getChildren().add(root);

            option1Selected.set(true);
            option2Selected.set(false);
            option3Selected.set(false);
            option4Selected.set(false);
            option5Selected.set(false);
            option6Selected.set(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadOption2Scene() {
        try {
            sceneLabel.setText("Xem thông tin chấm công");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/demo/userview/Option2Scene.fxml"));
            Parent root = loader.load();

            contentPane.getChildren().clear();
            contentPane.getChildren().add(root);

            option1Selected.set(false);
            option2Selected.set(true);
            option3Selected.set(false);
            option4Selected.set(false);
            option5Selected.set(false);
            option6Selected.set(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadOption3Scene() {
        try {
            sceneLabel.setText("Xem thông tin tổng hợp");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/demo/userview/Option2Scene.fxml"));
            Parent root = loader.load();

            contentPane.getChildren().clear();
            contentPane.getChildren().add(root);

            option1Selected.set(false);
            option2Selected.set(false);
            option3Selected.set(true);
            option4Selected.set(false);
            option5Selected.set(false);
            option6Selected.set(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadOption4Scene() {
        try {
            sceneLabel.setText("Tạo yêu cầu chỉnh sửa");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/demo/userview/Option2Scene.fxml"));
            Parent root = loader.load();

            contentPane.getChildren().clear();
            contentPane.getChildren().add(root);

            option1Selected.set(false);
            option2Selected.set(false);
            option3Selected.set(false);
            option4Selected.set(true);
            option5Selected.set(false);
            option6Selected.set(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadOption5Scene() {
        try {
            sceneLabel.setText("Xem yêu cầu chỉnh sửa");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/demo/userview/Option2Scene.fxml"));
            Parent root = loader.load();

            contentPane.getChildren().clear();
            contentPane.getChildren().add(root);

            option1Selected.set(false);
            option2Selected.set(false);
            option3Selected.set(false);
            option4Selected.set(false);
            option5Selected.set(true);
            option6Selected.set(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showLogoutConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("Click OK to confirm, or Cancel to go back.");

        ButtonType buttonTypeOK = new ButtonType("OK");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonType.CANCEL.getButtonData());
        alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeOK) {
                System.out.println("Logging out...");
                loadOption6Scene();
            } else {
                System.out.println("Cancelled logout");
            }
        });
    }
    private void loadOption6Scene() {
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

            Scene currentScene = contentPane.getScene();

            currentScene.setRoot(root);
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
        BooleanBinding option5SelectedBinding = Bindings.createBooleanBinding(
                option5Selected::get,
                option5Selected
        );
        BooleanBinding option6SelectedBinding = Bindings.createBooleanBinding(
                option6Selected::get,
                option6Selected
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
        option6Button.styleProperty().bind(
                Bindings.when(option6SelectedBinding)
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
}
