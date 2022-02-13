package com.prog3.servermail;

import com.prog3.servermail.controller.ServerController;
import com.prog3.servermail.model.ServerModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = mainLoader.load();
        ServerController serverController = mainLoader.getController();

        ServerModel serverModel = new ServerModel();
        serverModel.addLog("Server started!");
        serverController.initModel(serverModel, 8195);

        Scene scene = new Scene(root, 600, 350);

        stage.setTitle("MailServer");
        stage.setScene(scene);
        stage.setMinHeight(100);
        stage.setMinWidth(300);
        stage.show();

        stage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch();
    }
}
