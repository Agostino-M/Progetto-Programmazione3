package com.prog3.servermail.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class ServerModel {

    private final List<String> list = new ArrayList<>();
    private final ObservableList<String> logList = FXCollections.observableArrayList(list);

    private final ArrayList<String> usersConnected = new ArrayList<>();

    public ArrayList<String> getUsersConnected() {
        return usersConnected;
    }

    public ObservableList<String> getLogList() {
        return logList;
    }

    public synchronized void addLog(String line) {
        logList.add(line);
    }
}
