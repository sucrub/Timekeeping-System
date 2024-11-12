package com.example.demo.utils;

import com.example.demo.entity.User;
import com.example.demo.qlnssystem.QLNSSystemData;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class DBUtils {

    public static Connection connection = null;
    public static PreparedStatement preparedStatement = null;
    public static ResultSet result = null;
    public static User loginUser(String email, String password) {
        User loggedUser = null;
        try {
            connectDB();
            preparedStatement = connection.prepareStatement("select * FROM users WHERE email = ? and password = ?");
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            result = preparedStatement.executeQuery();
            loggedUser = QLNSSystemData.getUser(loggedUser, result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loggedUser;
    }

    public static Connection connectDB(){

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pm_chamcong", "root", ""); // root is the default username while "" or empty or null is the default password
            return connection;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void showMainView(String fxmlFilePath, String title) {
        try {
            Parent root = FXMLLoader.load(DBUtils.class.getResource("/com/example/demo/"+fxmlFilePath));
            Stage stage = new Stage();
            stage.setTitle(title);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
