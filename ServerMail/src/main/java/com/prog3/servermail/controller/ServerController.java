package com.prog3.servermail.controller;

import com.prog3.servermail.connection.Connection;
import com.prog3.servermail.model.ServerModel;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerController {

    @FXML
    private ListView<String> listView;
    private ServerModel serverModel;
    private ServerSocket s;
    private ExecutorService executor;
    private final static int NUM_THREADS = 10;
    private int port;

    public void initModel(ServerModel serverModel, Integer port) {
        this.port = port;
        if (this.serverModel != null)
            throw new IllegalStateException("Model can only be initialized once");
        this.serverModel = serverModel;

        listView.setItems(serverModel.getLogList());

        this.setClientConnection();
    }

    private void setClientConnection() {
        executor = Executors.newFixedThreadPool(NUM_THREADS);
        new Thread(() -> {
            try {
                s = new ServerSocket(port);
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true) {
                try {
                    Socket socket = s.accept();
                    executor.execute(new Connection(this.serverModel, socket));
                } catch (IOException e) {
                    e.printStackTrace();
                    executor.shutdown();
                }
            }

        }).start();

    }
}
