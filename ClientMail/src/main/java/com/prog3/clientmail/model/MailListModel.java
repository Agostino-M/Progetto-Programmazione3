package com.prog3.clientmail.model;

import com.prog3.common.Email;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class MailListModel {
    private final ObservableList<Email> emailList = FXCollections.observableArrayList();
    private final ObjectProperty<Email> currentMail = new SimpleObjectProperty<>(null);

    public ObservableList<Email> getEmailList() {
        return emailList;
    }

    public void addEmail(Email e) {
        emailList.add(e);
    }

    public void removeEmail(int id) {
        emailList.removeIf(email -> email.getId().equals(id));
    }

    public Email getCurrentMail() {
        return currentMail.get();
    }

    public ObjectProperty<Email> currentMailProperty() {
        return currentMail;
    }

    public void setCurrentMail(Email currentMail) {
        this.currentMail.set(currentMail);
    }

    public void loadData(ArrayList<Email> mails) { //per avere dei msg iniziali
        emailList.removeAll();
        emailList.setAll(mails);
    }

}

