package com.example.demo;

import com.example.demo.entity.User;
import com.example.demo.leaddepartment.LeadDepartmentView;
import com.example.demo.login.LoginView;
import com.example.demo.qlns.QLNSController;
import com.example.demo.qlns.QLNSView;
import com.example.demo.userview.UserMainView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/demo/login/login-view.fxml"));
        Scene scene = new Scene(root, 1255, 736);
        stage.setTitle("PMCM");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMaximized(false);
        stage.setMinHeight(720);
        stage.setMinWidth(1280);
        stage.show();
    }
    public static void redirectAfterLogin(User loggedUser) {
        int roleId = loggedUser.getRoleId();
        try {
            Parent root = null;
            FXMLLoader loader = null;
            if (roleId == 1) {
                loader = new FXMLLoader(App.class.getResource("/com/example/demo/qlns/QLNSScene.fxml"));
                root = loader.load();
                QLNSView ctrl = loader.getController();
                ctrl.setLoginUser(loggedUser);
                showMainView(root, "QLNS View");
            } else if (roleId == 2) {
                loader = new FXMLLoader(App.class.getResource("/com/example/demo/leaddepartment/LeadDepartmentScene.fxml"));
                root = loader.load();
                LeadDepartmentView ctrl = loader.getController();
                ctrl.setLoginUser(loggedUser);
                ctrl.setDepartmentId(loggedUser.getDepartmentId());
                showMainView(root, "Truong bo phan");
            } else if (roleId == 3) {
                loader = new FXMLLoader(App.class.getResource("/com/example/demo/userview/UserMainScene.fxml"));
                root = loader.load();
                UserMainView ctrl = loader.getController();
                ctrl.setLoginUser(loggedUser);
                showMainView(root, "Nhan vien");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void showMainView(Parent root, String title) {
            Stage stage = new Stage();
            stage.setTitle(title);

            Scene scene = new Scene(root);
            scene.setRoot(root);
            stage.setScene(scene);
            stage.show();
    }

    public static void logout(String email) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/example/demo/login/login-view.fxml"));
        Parent root = loader.load();
        LoginView ctrl = loader.getController();
        ctrl.setEmailField(email);
        showMainView(root, "Đăng nhập");
    }
    public static void main(String[] args) {
        try {
            Connection con = DbUtil.getConnection();
            launch();
            System.out.println("Connected to MySql Server.");
            con.close();
        } catch (SQLException ex) {
            System.out.println("Connection Error!");
        }
    }
}
