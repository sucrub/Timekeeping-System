package com.example.demo.entity;

import java.sql.Timestamp;

public class Log {
    private int id;
    private Timestamp log;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getLog() {
        return log;
    }

    public void setLog(Timestamp log) {
        this.log = log;
    }
}
