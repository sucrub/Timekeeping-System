package com.example.demo.entity;

public class ReportTicket {
    int id;
    int userId;
    String subject;
    String description;
    MyDate timeEncountered;
    boolean status;
    int ticketType;

    public ReportTicket(int id, int userId, String subject, String description, MyDate timeEncountered, boolean status, int ticketType) {
        this.id = id;
        this.userId = userId;
        this.subject = subject;
        this.description = description;
        this.timeEncountered = timeEncountered;
        this.status = status;
        this.ticketType = ticketType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MyDate getTimeEncountered() {
        return timeEncountered;
    }

    public void setTimeEncountered(MyDate timeEncountered) {
        this.timeEncountered = timeEncountered;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getTicketType() {
        return ticketType;
    }

    public void setTicketType(int ticketType) {
        this.ticketType = ticketType;
    }
}
