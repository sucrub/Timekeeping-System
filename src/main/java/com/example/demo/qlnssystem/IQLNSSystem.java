package com.example.demo.qlnssystem;

import com.example.demo.entity.Department;
import com.example.demo.entity.User;

import java.sql.SQLException;
import java.util.List;

public interface IQLNSSystem {
    List<Department> getAllDepartments() throws SQLException;

    Department getDepartmentById(int departmentId) throws SQLException;

    List<User> getUsersByDepartment(int departmentId) throws SQLException;

    List<User> getUsersIsOfficer() throws SQLException;

    User getUserById(int userId) throws SQLException;
}
