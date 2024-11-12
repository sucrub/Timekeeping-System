package com.example.demo.entity;

public class ReportTicketMessage {
    int id;
    int ticketId;
    int userId;
    String messageContent;

    public ReportTicketMessage(int id, int ticketId, int userId, String messageContent) {
        this.id = id;
        this.ticketId = ticketId;
        this.userId = userId;
        this.messageContent = messageContent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
