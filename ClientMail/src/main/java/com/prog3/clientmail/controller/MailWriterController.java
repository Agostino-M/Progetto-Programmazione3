package com.prog3.clientmail.controller;

import com.prog3.clientmail.connection.Connection;
import com.prog3.common.Email;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailWriterController {
    private Connection connection;
    private Stage stage;

    @FXML
    private TextField receivers;
    @FXML
    private TextField subject;
    @FXML
    private TextArea message;

    public void setReceivers(String receivers) {
        this.receivers.setText(receivers);
    }

    public void setSubject(String subject) {
        this.subject.setText(subject);
    }

    public void setMessage(String message) {
        this.message.setText(message);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private boolean validFields() {
        String receiversText = receivers.getText();
        ArrayList<String> receiversSplit = new ArrayList<>(Arrays.asList(receiversText.split(",")));

        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);

        for (String email : receiversSplit) {
            Matcher matcher = pattern.matcher(email);
            if (!matcher.matches()) {
                Alert warningAlert = new Alert(Alert.AlertType.ERROR, "Formato email non corretto!");
                warningAlert.initModality(Modality.APPLICATION_MODAL);
                warningAlert.getDialogPane().setHeaderText("Errore sul campo destinatari");
                warningAlert.initOwner(stage);
                warningAlert.showAndWait();
                return false;
            }
        }
        return true;
    }

    public void sendMail(MouseEvent mouseEvent) {
        if (!validFields()) {
            return;
        }

        String receiversText = receivers.getText();
        ArrayList<String> receiversSplit = new ArrayList<>(Arrays.asList(receiversText.split(",")));

        String subjectText = subject.getText().replace(":", "\\:");
        String messageText = message.getText();

        Date date = new Date();

        Email email = new Email(connection.getUsername(),
                receiversSplit,
                subjectText,
                messageText.replace("\n", "\\n").replace("\r", " "),
                date);
        if (connection.sendMail(email)) {
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Messaggio inviato!");
            successAlert.initModality(Modality.APPLICATION_MODAL);
            successAlert.initOwner(stage);
            successAlert.getDialogPane().setHeaderText("Messaggio inviato");
            successAlert.setTitle("Messaggio inviato");
            successAlert.showAndWait();
        } else {
            Alert warningAlert = new Alert(Alert.AlertType.WARNING, "Attenzione: qualche destinatario non esiste!");
            warningAlert.initModality(Modality.APPLICATION_MODAL);
            warningAlert.getDialogPane().setHeaderText("Attenzione");
            warningAlert.initOwner(stage);
            warningAlert.showAndWait();
        }
        clearFields();
    }

    private void clearFields() {
        this.receivers.setText("");
        this.subject.setText("");
        this.message.setText("");
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
