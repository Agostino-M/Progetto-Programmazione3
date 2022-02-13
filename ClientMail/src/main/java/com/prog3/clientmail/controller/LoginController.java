package com.prog3.clientmail.controller;

import com.prog3.clientmail.Main;
import com.prog3.clientmail.connection.Connection;
import com.prog3.clientmail.model.MailListModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    private Connection connection;
    private MailListModel mailListModel;
    private Stage stage;


    @FXML
    private void login(MouseEvent event) {
        String username = ((ImageView) event.getSource()).getId();
        System.out.println("Welcome " + username);

        // connessione al server
        connection.setUsername(username + "@mail.com");
        try {
            mailListModel.loadData(connection.loadData());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Errore nel primo caricamento dei dati");
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.initOwner(stage);
                alert.showAndWait();
            });
            e.printStackTrace();
        }

        try {
            setMainStage(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setMailListModel(MailListModel mailListModel) {
        this.mailListModel = mailListModel;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMainStage(Stage stage) throws IOException {
        BorderPane root = new BorderPane();
        FXMLLoader menuLoader = new FXMLLoader(Main.class.getResource("menu.fxml"));
        root.setLeft(menuLoader.load());
        MenuController menuController = menuLoader.getController();
        menuController.initModel(mailListModel);
        menuController.setConnection(connection);

        FXMLLoader readerLoader = new FXMLLoader(Main.class.getResource("reader.fxml"));
        Node reader = readerLoader.load();
        root.setCenter(reader);
        MailReaderController readerController = readerLoader.getController();
        readerController.initModel(mailListModel);
        readerController.setConnection(connection);


        FXMLLoader writerLoader = new FXMLLoader(Main.class.getResource("writer.fxml"));
        Node writer = writerLoader.load();
        MailWriterController writerController = writerLoader.getController();
        writerController.setConnection(connection);
        writerController.setStage(stage);

        readerController.setMailWriterController(writerController);
        menuController.setMailWriterController(writerController);
        menuController.setScene(root, writer, reader);
        readerController.setScene(root, writer);

        Scene scene = new Scene(root);

        stage.setTitle(connection.getUsername() + " - ClientMail");
        stage.setScene(scene);

        stage.setMaxWidth(2800);
        stage.setMaxHeight(1800);
        stage.setMinWidth(700);
        stage.setMinHeight(400);
        stage.setWidth(1000);
        stage.setHeight(600);
        stage.show();
    }
}
