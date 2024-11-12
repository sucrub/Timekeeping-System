package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {

    public static Connection getConnection() throws SQLException {
        System.out.println("Connected to MySql Server.");
        String url = "jdbc:mysql://localhost:3306/pm_chamcong";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }
}
