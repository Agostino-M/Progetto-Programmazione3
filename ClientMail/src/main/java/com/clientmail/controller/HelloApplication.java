package com.clientmail.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        BorderPane root = new BorderPane();
        stage.setTitle("Posta elettronica");

        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("menu.fxml"));
        root.setLeft(menuLoader.load());

        FXMLLoader writerLoader = new FXMLLoader(getClass().getResource("writer.fxml"));
        //root.setCenter(writerLoader.load());

        FXMLLoader readerLoader = new FXMLLoader(getClass().getResource("reader.fxml"));
        root.setCenter(readerLoader.load());

        // TODO aggiungere anchor pane con dentro un borderpane su cui inserire menu, reader/writer xd :)
        Scene scene = new Scene(root, 1000, 600);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
