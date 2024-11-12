package com.example.demo.log;

import com.example.demo.DbUtil;
import com.example.demo.entity.MyDate;
import com.example.demo.entity.OfficerLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OfficerLogControl {
    public static ResultSet result = null;
    public static PreparedStatement preparedStatement = null;

    public void createOfficerLog(Connection connection, LocalDate localDate, List<Boolean> booleanList, Double timeLate, Double timeEarly, int userId)
            throws SQLException {
        if (connection == null) {
            connection = DbUtil.getConnection();
        }
        String sql = "INSERT INTO officer_logs (day, morning, afternoon, timeLate, timeEarly, userId) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            preparedStatement.setBoolean(2, booleanList.get(0));
            preparedStatement.setBoolean(3, booleanList.get(1));
            preparedStatement.setDouble(4, timeLate);
            preparedStatement.setDouble(5, timeEarly);
            preparedStatement.setInt(6, userId);

            preparedStatement.executeUpdate();
        }
    }

    public OfficerLog getOfficerLogByUserIdAndDate(Connection connection, int userId, String date) throws SQLException {
        if (connection == null) {
            connection = DbUtil.getConnection();
        }
        OfficerLog officerLog = null;
        String sql = "SELECT * FROM officer_logs WHERE userId = ? AND day = ? LIMIT 1";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, userId);
        preparedStatement.setString(2, date);
        result = preparedStatement.executeQuery();
        try {
            if (result.next()) {
                officerLog = new OfficerLog(result.getInt("id"),
                        new MyDate(result.getString("day")),
                        result.getBoolean("morning"),
                        result.getBoolean("afternoon"),
                        result.getDouble("timeLate"),
                        result.getDouble("timeEarly"));
            }
        } catch (SQLException ex) {
            System.out.println("ERROR : " + ex.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return officerLog;
    }

    public void editOfficerLog(Connection connection, int id, List<Boolean> booleanList, double timeLate, double timeEarly) throws SQLException {
        if (connection == null) {
            connection = DbUtil.getConnection();
        }
        String sql = "UPDATE officer_logs " +
                "SET morning = ?, afternoon = ?, timeLate = ?, timeEarly = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBoolean(1, booleanList.get(0));
            preparedStatement.setBoolean(2, booleanList.get(1));
            preparedStatement.setDouble(3, timeLate);
            preparedStatement.setDouble(4, timeEarly);
            preparedStatement.setInt(5, id);

            preparedStatement.executeUpdate();
        }
    }
}
