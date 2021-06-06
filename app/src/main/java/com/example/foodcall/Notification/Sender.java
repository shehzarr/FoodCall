package com.example.foodcall.Notification;

public class Sender {

    private Data_Notification dataNotification;
    String to;

    public Sender(Data_Notification dataNotification, String to) {
        this.dataNotification = dataNotification;
        this.to = to;
    }

    public Data_Notification getDataNotification() {
        return dataNotification;
    }

    public void setDataNotification(Data_Notification dataNotification) {
        this.dataNotification = dataNotification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
