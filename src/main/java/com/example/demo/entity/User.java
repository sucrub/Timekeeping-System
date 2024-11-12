package com.example.demo.entity;

import java.util.Date;
public class User {
    private int id;
    private String name;
    private String password;
    private int roleId;
    private MyDate birthday;
    private String email;
    private String address;
    private int departmentId;

    public User(int id, String name, int roleId) {
        this.id = id;
        this.name = name;
        this.roleId = roleId;
    }
  
    public User(int id, String name, String password, int roleId, MyDate birthday, String email, String address, int departmentId) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.roleId = roleId;
        this.birthday = birthday;
        this.email = email;
        this.address = address;
        this.departmentId = departmentId;
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
        this.password = "1";
        this.roleId = 1;
        this.birthday = new MyDate(2023,8,11);
        this.email = "test@gmail.com";
        this.address="Hanoi";
        this.departmentId = 1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public MyDate getBirthday() {
        return birthday;
    }

    public void setBirthday(MyDate birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", roleId=" + roleId +
                ", birthday=" + birthday +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", departmentId=" + departmentId +
                '}';
    }
}
