package com.example.demo.log;

import com.example.demo.DbUtil;
import com.example.demo.entity.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogControl {

    public List<WorkerLog> getWorkerLogData(List<User> listUser, int month, int year) {
        List<WorkerLog> data = new ArrayList<>();
        try {
            Connection con = DbUtil.getConnection();
            PreparedStatement query = con.prepareStatement("SELECT * FROM worker_logs WHERE MONTH(day) = ? AND YEAR(day) = ? AND userId = ? ORDER BY userId, day");
            double shift1Sum;
            double shift2Sum;
            double shift3Sum;
            for (User user : listUser) {
                query.setInt(1, month);
                query.setInt(2, year);
                query.setInt(3, user.getId());
                ResultSet result = query.executeQuery();
                shift1Sum = 0.0;
                shift2Sum = 0.0;
                shift3Sum = 0.0;
                while (result.next()) {
                    shift1Sum += result.getDouble("shift1");
                    shift2Sum += result.getDouble("shift2");
                    shift3Sum += result.getDouble("shift3");
                }
                WorkerLog log = new WorkerLog(user.getId(), user.getName(), shift1Sum, shift2Sum, shift3Sum);
                data.add(log);
                result.close();
            }
            con.close();
        } catch (SQLException ex) {
            System.out.println("Connection Error!");
        }
        return data;
    }

    public List<WorkerLog> getWorkerLogData() {
        List<WorkerLog> data = new ArrayList<>();
        try {
            Connection con = DbUtil.getConnection();
            PreparedStatement query = con.prepareStatement("SELECT * FROM worker_logs ORDER BY userId, day");
            try (ResultSet result = query.executeQuery()) {
                while (result.next()) {
                    WorkerLog log = new WorkerLog(
                            result.getInt("userId"),
                            new MyDate(result.getDate("day").getTime()),
                            result.getDouble("shift1"),
                            result.getDouble("shift2"),
                            result.getDouble("shift3"));
                    data.add(log);
                }
            } catch (SQLException ex) {
                System.out.println("ERROR : " + ex.getMessage());
            }
            con.close();
        } catch (SQLException ex) {
            System.out.println("Connection Error!");
        }
        return data;
    }

    public double getSumShift1(int year) {
        double sumShift1 = 0.0;

        try (Connection con = DbUtil.getConnection();
             PreparedStatement query = con.prepareStatement(
                     "SELECT SUM(shift1) AS sumShift1 " +
                             "FROM worker_logs WHERE YEAR(day) = ? ")) {

            query.setInt(1, year);
            try (ResultSet result = query.executeQuery()) {
                if (result.next()) {
                    sumShift1 = result.getDouble("sumShift1");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return sumShift1;
    }

    public double getSumShift2(int year) {
        double sumShift2 = 0.0;

        try (Connection con = DbUtil.getConnection();
             PreparedStatement query = con.prepareStatement(
                     "SELECT SUM(shift2) AS sumShift2 " +
                             "FROM worker_logs WHERE YEAR(day) = ? ")) {

            query.setInt(1, year);
            try (ResultSet result = query.executeQuery()) {
                if (result.next()) {
                    sumShift2 = result.getDouble("sumShift2");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return sumShift2;
    }

    public double getSumShift3(int year) {
        double sumShift3 = 0.0;

        try (Connection con = DbUtil.getConnection();
             PreparedStatement query = con.prepareStatement(
                     "SELECT SUM(shift3) AS sumShift3 " +
                             "FROM worker_logs WHERE YEAR(day) = ? ")) {

            query.setInt(1, year);
            try (ResultSet result = query.executeQuery()) {
                if (result.next()) {
                    sumShift3 = result.getDouble("sumShift3");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return sumShift3;
    }

    public double getSumShift1(int month, int year) {
        double sumShift1 = 0.0;

        try (Connection con = DbUtil.getConnection();
             PreparedStatement query = con.prepareStatement(
                     "SELECT SUM(shift1) AS sumShift1 " +
                             "FROM worker_logs " +
                             "WHERE MONTH(day) = ? AND YEAR(day) = ?")) {

            query.setInt(1, month);
            query.setInt(2, year);

            try (ResultSet result = query.executeQuery()) {
                if (result.next()) {
                    sumShift1 = result.getDouble("sumShift1");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return sumShift1;
    }

    public double getSumShift2(int month, int year) {
        double sumShift2 = 0.0;

        try (Connection con = DbUtil.getConnection();
             PreparedStatement query = con.prepareStatement(
                     "SELECT SUM(shift2) AS sumShift2 " +
                             "FROM worker_logs " +
                             "WHERE MONTH(day) = ? AND YEAR(day) = ?")) {

            query.setInt(1, month);
            query.setInt(2, year);

            try (ResultSet result = query.executeQuery()) {
                if (result.next()) {
                    sumShift2 = result.getDouble("sumShift2");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return sumShift2;
    }

    public double getSumShift3(int month, int year) {
        double sumShift3 = 0.0;

        try (Connection con = DbUtil.getConnection();
             PreparedStatement query = con.prepareStatement(
                     "SELECT SUM(shift3) AS sumShift3 " +
                             "FROM worker_logs " +
                             "WHERE MONTH(day) = ? AND YEAR(day) = ?")) {

            query.setInt(1, month);
            query.setInt(2, year);

            try (ResultSet result = query.executeQuery()) {
                if (result.next()) {
                    sumShift3 = result.getDouble("sumShift3");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return sumShift3;
    }


    public double getSumShift1Quad(int quad, int year) {
        double sumShift1 = 0.0;

        try (Connection con = DbUtil.getConnection();
             PreparedStatement query = con.prepareStatement(
                     "SELECT SUM(shift1) AS sumShift1 " +
                             "FROM worker_logs " +
                             "WHERE MONTH(day) IN (?, ?, ?) AND YEAR(day) = ?")) {

            int startMonth = (quad - 1) * 3 + 1;
            query.setInt(1, startMonth);
            query.setInt(2, startMonth + 1);
            query.setInt(3, startMonth + 2);
            query.setInt(4, year);

            try (ResultSet result = query.executeQuery()) {
                if (result.next()) {
                    sumShift1 = result.getDouble("sumShift1");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return sumShift1;
    }

    public double getSumShift2Quad(int quad, int year) {
        double sumShift2 = 0.0;

        try (Connection con = DbUtil.getConnection();
             PreparedStatement query = con.prepareStatement(
                     "SELECT SUM(shift2) AS sumShift2 " +
                             "FROM worker_logs " +
                             "WHERE MONTH(day) IN (?, ?, ?) AND YEAR(day) = ?")) {

            int startMonth = (quad - 1) * 3 + 1;
            query.setInt(1, startMonth);
            query.setInt(2, startMonth + 1);
            query.setInt(3, startMonth + 2);
            query.setInt(4, year);

            try (ResultSet result = query.executeQuery()) {
                if (result.next()) {
                    sumShift2 = result.getDouble("sumShift2");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return sumShift2;
    }

    public double getSumShift3Quad(int quad, int year) {
        double sumShift3 = 0.0;

        try (Connection con = DbUtil.getConnection();
             PreparedStatement query = con.prepareStatement(
                     "SELECT SUM(shift3) AS sumShift3 " +
                             "FROM worker_logs " +
                             "WHERE MONTH(day) IN (?, ?, ?) AND YEAR(day) = ?")) {

            int startMonth = (quad - 1) * 3 + 1;
            query.setInt(1, startMonth);
            query.setInt(2, startMonth + 1);
            query.setInt(3, startMonth + 2);
            query.setInt(4, year);

            try (ResultSet result = query.executeQuery()) {
                if (result.next()) {
                    sumShift3 = result.getDouble("sumShift3");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return sumShift3;
    }

    public List<OfficerLog> getOfficerLogData(List<User> listUser, int month, int year) {
        List<OfficerLog> data = new ArrayList<>();
        try {
            Connection con = DbUtil.getConnection();
            PreparedStatement query = con.prepareStatement("SELECT * FROM officer_logs WHERE MONTH(day) = ? AND YEAR(day) = ? AND userId = ? ORDER BY userId, day");
            int sumWorkSession;
            double sumEarlyTime;
            double sumLateTime;
            for (User user : listUser) {
                query.setInt(1, month);
                query.setInt(2, year);
                query.setInt(3, user.getId());
                ResultSet result = query.executeQuery();
                sumWorkSession = 0;
                sumEarlyTime = 0.0;
                sumLateTime = 0.0;
                while (result.next()) {
                    if (result.getBoolean("morning")) sumWorkSession += 1;
                    if (result.getBoolean("afternoon")) sumWorkSession += 1;
                    sumEarlyTime += result.getDouble("timeEarly");
                    sumLateTime += result.getDouble("timeLate");
                }
                OfficerLog log = new OfficerLog(user.getId(), user.getName(), sumWorkSession, sumEarlyTime, sumLateTime);
                data.add(log);
                result.close();
            }
            con.close();
        } catch (SQLException ex) {
            System.out.println("Connection Error!");
        }
        return data;
    }

    public List<OfficerLog> getOfficerLogData(int departmentId) {
        List<OfficerLog> data = new ArrayList<>();
        try {
            Connection con = DbUtil.getConnection();
            PreparedStatement query = con.prepareStatement("SELECT ol.* " +
                    "FROM officer_logs ol " +
                    "JOIN users u ON ol.userId = u.id " +
                    "WHERE u.departmentId = ?" +
                    " ORDER BY userId, day");
            query.setInt(1, departmentId);
            try (ResultSet result = query.executeQuery()) {
                while (result.next()) {
                    OfficerLog log = new OfficerLog(
                            result.getInt("userId"),
                            new MyDate(result.getDate("day").getTime()),
                            result.getBoolean("morning"),
                            result.getBoolean("afternoon"),
                            result.getDouble("timeLate"),
                            result.getDouble("timeEarly"));
                    data.add(log);
                }
            } catch (SQLException ex) {
                System.out.println("ERROR : " + ex.getMessage());
            }
            con.close();
        } catch (SQLException ex) {
            System.out.println("Connection Error!");
        }
        return data;
    }

    public double getSumTimeLate(int departmentId, int year) {
        double sumTimeLate = 0.0;

        try (Connection con = DbUtil.getConnection();
             PreparedStatement query = con.prepareStatement(
                     "SELECT SUM(timeLate) AS sumTimeLate " +
                             "FROM officer_logs ol " +
                             "JOIN users u ON ol.userId = u.id " +
                             "WHERE u.departmentId = ? AND YEAR(ol.day) = ?")) {

            query.setInt(1, departmentId);
            query.setInt(2, year);

            try (ResultSet result = query.executeQuery()) {
                if (result.next()) {
                    sumTimeLate = result.getDouble("sumTimeLate");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return sumTimeLate;
    }

    public double getSumTimeEarly(int departmentId, int year) {
        double sumTimeEarly = 0.0;

        try (Connection con = DbUtil.getConnection();
             PreparedStatement query = con.prepareStatement(
                     "SELECT SUM(timeEarly) AS sumTimeEarly " +
                             "FROM officer_logs ol " +
                             "JOIN users u ON ol.userId = u.id " +
                             "WHERE u.departmentId = ? AND YEAR(ol.day) = ?")) {

            query.setInt(1, departmentId);
            query.setInt(2, year);

            try (ResultSet result = query.executeQuery()) {
                if (result.next()) {
                    sumTimeEarly = result.getDouble("sumTimeEarly");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return sumTimeEarly;
    }


    public double getSumTimeLate(int departmentId, int month, int year) {
        double sumTimeLate = 0.0;

        try (Connection con = DbUtil.getConnection();
             PreparedStatement query = con.prepareStatement(
                     "SELECT SUM(timeLate) AS sumTimeLate " +
                             "FROM officer_logs ol " +
                             "JOIN users u ON ol.userId = u.id " +
                             "WHERE u.departmentId = ? AND MONTH(ol.day) = ? AND YEAR(ol.day) = ?")) {

            query.setInt(1, departmentId);
            query.setInt(2, month);
            query.setInt(3, year);

            try (ResultSet result = query.executeQuery()) {
                if (result.next()) {
                    sumTimeLate = result.getDouble("sumTimeLate");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return sumTimeLate;
    }

    public double getSumTimeEarly(int departmentId, int month, int year) {
        double sumTimeEarly = 0.0;

        try (Connection con = DbUtil.getConnection();
             PreparedStatement query = con.prepareStatement(
                     "SELECT SUM(timeEarly) AS sumTimeEarly " +
                             "FROM officer_logs ol " +
                             "JOIN users u ON ol.userId = u.id " +
                             "WHERE u.departmentId = ? AND MONTH(ol.day) = ? AND YEAR(ol.day) = ?")) {

            query.setInt(1, departmentId);
            query.setInt(2, month);
            query.setInt(3, year);

            try (ResultSet result = query.executeQuery()) {
                if (result.next()) {
                    sumTimeEarly = result.getDouble("sumTimeEarly");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return sumTimeEarly;
    }


    public double getSumTimeLateQuad(int departmentId, int quad, int year) {
        double sumTimeLate = 0.0;

        try (Connection con = DbUtil.getConnection();
             PreparedStatement query = con.prepareStatement(
                     "SELECT SUM(timeLate) AS sumTimeLate " +
                             "FROM officer_logs ol " +
                             "JOIN users u ON ol.userId = u.id " +
                             "WHERE u.departmentId = ? AND MONTH(ol.day) IN (?, ?, ?) AND YEAR(ol.day) = ?")) {

            int startMonth = (quad - 1) * 3 + 1;
            query.setInt(1, departmentId);
            query.setInt(2, startMonth);
            query.setInt(3, startMonth + 1);
            query.setInt(4, startMonth + 2);
            query.setInt(5, year);

            try (ResultSet result = query.executeQuery()) {
                if (result.next()) {
                    sumTimeLate = result.getDouble("sumTimeLate");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return sumTimeLate;
    }

    public double getSumTimeEarlyQuad(int departmentId, int quad, int year) {
        double sumTimeEarly = 0.0;

        try (Connection con = DbUtil.getConnection();
             PreparedStatement query = con.prepareStatement(
                     "SELECT SUM(timeEarly) AS sumTimeEarly " +
                             "FROM officer_logs ol " +
                             "JOIN users u ON ol.userId = u.id " +
                             "WHERE u.departmentId = ? AND MONTH(ol.day) IN (?, ?, ?) AND YEAR(ol.day) = ?")) {

            int startMonth = (quad - 1) * 3 + 1;
            query.setInt(1, departmentId);
            query.setInt(2, startMonth);
            query.setInt(3, startMonth + 1);
            query.setInt(4, startMonth + 2);
            query.setInt(5, year);

            try (ResultSet result = query.executeQuery()) {
                if (result.next()) {
                    sumTimeEarly = result.getDouble("sumTimeEarly");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return sumTimeEarly;
    }

    public void createLog(Connection connection, int userId, Timestamp timestamp) throws SQLException {
        String sql = "INSERT INTO logs (userId, log) VALUES (?, ?)";

        if (connection == null) {
            connection = DbUtil.getConnection();
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setTimestamp(2, timestamp);

            preparedStatement.executeUpdate();
        }
    }

}
