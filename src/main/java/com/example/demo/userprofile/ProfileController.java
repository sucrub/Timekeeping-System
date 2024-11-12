package com.example.demo.userprofile;

import com.example.demo.entity.Department;
import com.example.demo.entity.User;
import com.example.demo.qlnssystem.IQLNSSystem;
import com.example.demo.qlnssystem.QLNSSystemData;

import java.sql.SQLException;

public class ProfileController {
    IQLNSSystem qlnsSystem = new QLNSSystemData();
    User user;
    public ProfileController(User user) {
        System.out.println(user.toString() + "Test controller");
        this.user = user;
    }

    public User getUserData() {
        return user;
    }

    public String getDepartmentName() throws SQLException {
        Department department = qlnsSystem.getDepartmentById(user.getDepartmentId());
        return department.getName();
    }
}
