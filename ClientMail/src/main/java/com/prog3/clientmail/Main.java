package com.prog3.clientmail;

import com.prog3.clientmail.connection.Connection;
import com.prog3.clientmail.controller.LoginController;
import com.prog3.clientmail.model.MailListModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    MailListModel mailListModel = new MailListModel();
    private final int SERVER_PORT = 8195;
    Connection connection = new Connection(SERVER_PORT, mailListModel);

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loginLoader.load();
        LoginController loginController = loginLoader.getController();
        loginController.setMailListModel(mailListModel);
        loginController.setConnection(connection);

        Scene scene = new Scene(root);
        loginController.setStage(stage);

        stage.setTitle("Login");
        stage.setMaxHeight(400);
        stage.setMaxWidth(600);
        stage.setMinHeight(400);
        stage.setMinWidth(600);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(t -> {
            connection.closeConnection();

            Platform.exit();
            System.exit(0);
        });

    }

    public static void main(String[] args) {
        launch();
    }
}
