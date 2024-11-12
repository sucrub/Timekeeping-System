package com.example.demo.entity;

public class OfficerLog extends Log {
    private int id; // for userId
    private MyDate day;
    private String name;



    private int sumWorkSession;
    private boolean morning;
    private boolean afternoon;
    private double timeLate;
    private double timeEarly;

    public OfficerLog(int id, MyDate day, boolean morning, boolean afternoon, double timeLate, double timeEarly) {
        this.id = id;
        this.day = day;
        this.morning = morning;
        this.afternoon = afternoon;
        this.timeLate = timeLate;
        this.timeEarly = timeEarly;
    }
    public OfficerLog(int id, String name, int sumWorkSession, double timeLate, double timeEarly) {
        this.id = id;
        this.name = name;
        this.sumWorkSession = sumWorkSession;
        this.timeLate = timeLate;
        this.timeEarly = timeEarly;
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

    public boolean isMorning() {
        return morning;
    }

    public void setMorning(boolean morning) {
        this.morning = morning;
    }

    public boolean isAfternoon() {
        return afternoon;
    }

    public void setAfternoon(boolean afternoon) {
        this.afternoon = afternoon;
    }

    public double getTimeLate() {
        return timeLate;
    }

    public void setTimeLate(double timeLate) {
        this.timeLate = timeLate;
    }

    public double getTimeEarly() {
        return timeEarly;
    }

    public void setTimeEarly(double timeEarly) {
        this.timeEarly = timeEarly;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getSumWorkSession() {
        return sumWorkSession;
    }

    public void setSumWorkSession(int sumWorkSession) {
        this.sumWorkSession = sumWorkSession;
    }
    @Override
    public String toString() {
        return "OfficerLog{" +
                "id=" + id +
                ", day='" + day + '\'' +
                ", morning=" + morning +
                ", afternoon=" + afternoon +
                ", timeLate=" + timeLate +
                ", timeEarly=" + timeEarly +
                '}';
    }
}
