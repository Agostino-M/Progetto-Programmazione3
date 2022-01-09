package com.example.model;

import com.example.comunication.Email;
import java.util.ArrayList;

public class Account {
    private String email;
    private ArrayList<Email> inboxEmails;

    public Account() {
    }

    public Account(String email, String password) {
        this.email = email;
        inboxEmails = null;
    }

    public Account(String email, String password, ArrayList<Email> inboxEmails) {
        this.email = email;
        this.inboxEmails = inboxEmails;
    }

    public String getEmail() {
        return email;
    }


    public ArrayList<Email> getInboxMessages() {
        return inboxEmails;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setInboxMessages(ArrayList<Email> inboxEmails) {
        this.inboxEmails = inboxEmails;
    }
}
