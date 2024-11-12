package com.example.demo.login;

import com.example.demo.App;
import com.example.demo.entity.User;
import com.example.demo.utils.DBUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginView implements Initializable {

    @FXML
    private Label titleLabel;

    @FXML
    private TextField passwordUnmask;

    @FXML
    private Button togglePassword;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button loginButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
    @FXML
    protected void onLoginButtonClick() {

        try {

            Alert alert;
            if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng điền email và mật khẩu");
                alert.showAndWait();
            } else {
                User loginUser = DBUtils.loginUser(usernameField.getText(), passwordField.getText());

                if (loginUser != null) {

                    System.out.println(loginUser);
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText(null);
                    alert.setContentText("Đăng nhập thành công!");
                    alert.showAndWait();

                    loginButton.getScene().getWindow().hide();

                    // SHOW THE MAIN FORM
                    App.redirectAfterLogin(loginUser);

                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Tên đăng nhập hoặc mật khẩu không đúng");
                    alert.showAndWait();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void setEmailField(String email) {
        usernameField.setText(email);
    }

    @FXML
    protected void onShowPassword() {
        passwordUnmask.setText(passwordField.getText());
        passwordField.setVisible(false);
        togglePassword.setText("Hide");
        passwordUnmask.setVisible(true);
    }
    @FXML
    protected void onHidePassword() {
        passwordField.setVisible(true);
        togglePassword.setText("Show");
        passwordUnmask.setVisible(false);
    }
}
