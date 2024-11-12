package com.example.demo.entity;

import java.util.Date;

public class WorkerLog extends Log {
    private int id; // for userId
    private MyDate day;
    private String name;
    private double shift1;
    private double shift2;
    private double shift3;

    public WorkerLog(int id, MyDate day, double shift1, double shift2, double shift3) {
        this.id = id;
        this.day = day;
        this.shift1 = shift1;
        this.shift2 = shift2;
        this.shift3 = shift3;
    }

    public WorkerLog(int id, String name, double shift1, double shift2, double shift3) {
        this.id = id;
        this.name = name;
        this.shift1 = shift1;
        this.shift2 = shift2;
        this.shift3 = shift3;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MyDate getDay() {
        return day;
    }

    public void setDay(MyDate day) {
        this.day = day;
    }

    public double getShift1() {
        return shift1;
    }

    public void setShift1(double shift1) {
        this.shift1 = shift1;
    }

    public double getShift2() {
        return shift2;
    }

    public void setShift2(double shift2) {
        this.shift2 = shift2;
    }

    public double getShift3() {
        return shift3;
    }

    public void setShift3(double shift3) {
        this.shift3 = shift3;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        // Create a meaningful string representation of the object
        return "WorkerLog{" +
                "userId=" + getId() +
                ", day=" + getDay() +
                ", shift1=" + getShift1() +
                ", shift2=" + getShift2() +
                ", shift3=" + getShift3() +
                '}';
    }
}
