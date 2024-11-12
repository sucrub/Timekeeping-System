package com.example.demo.log;

import com.example.demo.DbUtil;
import com.example.demo.entity.MyDate;
import com.example.demo.entity.WorkerLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WorkerLogControl {
    public static ResultSet result = null;
    public static PreparedStatement preparedStatement = null;
    public void createWorkerLog(Connection connection, LocalDate localDate, List<Double> doubleList, int userId) throws SQLException {
        if (connection == null) {
            connection = DbUtil.getConnection();
        }
        String sql = "INSERT INTO worker_logs (day, shift1, shift2, shift3, userId) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            preparedStatement.setDouble(2, doubleList.get(0));
            preparedStatement.setDouble(3, doubleList.get(1));
            preparedStatement.setDouble(4, doubleList.get(2));
            preparedStatement.setInt(5, userId);

            preparedStatement.executeUpdate();
        }
    }

    public WorkerLog getWorkerLogByUserIdAndDate(Connection connection, int userId, String date) throws SQLException {
        if (connection == null) {
            connection = DbUtil.getConnection();
        }
        WorkerLog workerLog = null;
        String sql = "SELECT * FROM worker_logs WHERE userId = ? AND day = ? LIMIT 1";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, userId);
        preparedStatement.setString(2, date);
        result = preparedStatement.executeQuery();
        try {
            if (result.next()) {
                workerLog = new WorkerLog(result.getInt("id"), new MyDate(result.getString("day")), result.getDouble("shift1"), result.getDouble("shift2"), result.getDouble("shift3"));
            }
        } catch (SQLException ex) {
            System.out.println("ERROR : " + ex.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return workerLog;
    }

    public void editWorkerLog(Connection connection, int id, List<Double> doubleList) throws SQLException {
        if (connection == null) {
            connection = DbUtil.getConnection();
        }
        String sql = "UPDATE worker_logs " +
                "SET shift1 = ?, shift2 = ?, shift3 = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, doubleList.get(0));
            preparedStatement.setDouble(2, doubleList.get(1));
            preparedStatement.setDouble(3, doubleList.get(2));
            preparedStatement.setDouble(4, id);

            preparedStatement.executeUpdate();
        }
    }
}
