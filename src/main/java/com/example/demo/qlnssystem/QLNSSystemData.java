package com.example.demo.qlnssystem;

import com.example.demo.DbUtil;
import com.example.demo.entity.Department;
import com.example.demo.entity.MyDate;
import com.example.demo.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QLNSSystemData implements IQLNSSystem {
    public static Connection connection = null;
    public static PreparedStatement preparedStatement = null;
    public static ResultSet result = null;

    @Override
    public List<Department> getAllDepartments() throws SQLException {
        List<Department> departments = new ArrayList<>();
        connection = DbUtil.getConnection();
        preparedStatement = connection.prepareStatement("select * FROM departments");
        result = preparedStatement.executeQuery();
        try {
            while (result.next()) {
                departments.add(new Department(result.getInt("id"), result.getString("name")));
            }
        } catch (SQLException ex) {
            System.out.println("ERROR : " + ex.getMessage());
        }
        return departments;
    }

    @Override
    public Department getDepartmentById(int departmentId) throws SQLException {
        Department department = null;
        connection = DbUtil.getConnection();
        preparedStatement = connection.prepareStatement("select * FROM departments where id = ?");
        preparedStatement.setInt(1, departmentId);
        result = preparedStatement.executeQuery();
        try {
            if (result.next()) {
                department = new Department(result.getInt("id"), result.getString("name"));
            }
        } catch (SQLException ex) {
            System.out.println("ERROR : " + ex.getMessage());
        }
        return department;
    }

    @Override
    public List<User> getUsersByDepartment(int departmentId) throws SQLException {
        List<User> users = new ArrayList<>();
        connection = DbUtil.getConnection();
        preparedStatement = connection.prepareStatement("select  * from users where departmentId = ?");
        preparedStatement.setString(1, String.valueOf(departmentId));
        return getUsers(users);
    }

    @Override
    public List<User> getUsersIsOfficer() throws SQLException {
        List<User> users = new ArrayList<>();
        connection = DbUtil.getConnection();
        preparedStatement = connection.prepareStatement("select  * from users where departmentId <> 1");
        return getUsers(users);
    }

    @Override
    public User getUserById(int userId) throws SQLException {
        User user = null;
        connection = DbUtil.getConnection();
        preparedStatement = connection.prepareStatement("select  * from users where id = ?");
        preparedStatement.setString(1, String.valueOf(userId));
        result = preparedStatement.executeQuery();
        user = getUser(user, result);
        return user;
    }

    public static User getUser(User user, ResultSet result) {
        try {
            if (result.next()) {
                user = new User(result.getInt("id"),
                        result.getString("name"),
                        result.getString("password"),
                        result.getInt("roleId"),
                        new MyDate(result.getDate("birthday").getTime()),
                        result.getString("email"),
                        result.getString("address"),
                        result.getInt("departmentId"));
            }
        } catch (SQLException ex) {
            System.out.println("ERROR : " + ex.getMessage());
        }
        return user;
    }

    private List<User> getUsers(List<User> users) throws SQLException {
        result = preparedStatement.executeQuery();
        try {
            while (result.next()) {
                users.add(new User(result.getInt("id"),
                        result.getString("name"),
                        result.getString("password"),
                        result.getInt("roleId"),
                        new MyDate(result.getDate("birthday").getTime()),
                        result.getString("email"),
                        result.getString("address"),
                        result.getInt("departmentId")));
            }
        } catch (SQLException ex) {
            System.out.println("ERROR : " + ex.getMessage());
        }
        return users;
    }
}
