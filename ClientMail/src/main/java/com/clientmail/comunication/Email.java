package com.clientmail.comunication;

import java.io.Serializable;
import java.util.ArrayList;

public class Email implements Serializable {
    private String id;
    private String sender;
    private ArrayList<String> receivers;
    private String subject;
    private String message;
    private String data;

    public Email() {
        this.id = "";
        this.sender = "";
        this.receivers = new ArrayList<>();
        this.subject = "";
        this.message = "";
        this.data = "";
    }

    public Email(String sender, ArrayList<String> receivers, String subject, String message, String data) {
        this.sender = sender;
        this.receivers = receivers;
        this.subject = subject;
        this.message = message;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public ArrayList<String> getReceivers() {
        return receivers;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceivers(ArrayList<String> receivers) {
        this.receivers = receivers;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Email{" +
                "id='" + id + '\'' +
                ", sender='" + sender + '\'' +
                ", receivers=" + receivers +
                ", subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
