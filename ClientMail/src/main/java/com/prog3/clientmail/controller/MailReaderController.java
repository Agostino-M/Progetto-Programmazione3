package com.prog3.clientmail.controller;

import com.prog3.clientmail.connection.Connection;
import com.prog3.clientmail.model.MailListModel;
import com.prog3.common.Email;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

public class MailReaderController {
    private Connection connection;
    private MailWriterController mailWriterController;
    private MailListModel mailListModel;
    private BorderPane root;
    private Node writer;


    @FXML
    private Label sender;
    @FXML
    private Label receiver;
    @FXML
    private Label subject;
    @FXML
    private Label message;
    @FXML
    private Label data;

    public void setMailWriterController(MailWriterController mailWriterController) {
        this.mailWriterController = mailWriterController;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setScene(BorderPane root, Node writer) {
        this.root = root;
        this.writer = writer;
    }

    /**
     * delete email
     */
    @FXML
    private void deleteEmail() {
        Integer id = mailListModel.getCurrentMail().getId();
        connection.deleteMail(id);
    }

    /**
     * forward the selected email
     * @param mouseEvent
     */
    @FXML
    public void forwardEmail(MouseEvent mouseEvent) {
        root.setCenter(writer);

        Email email = mailListModel.getCurrentMail();
        mailWriterController.setReceivers("");
        mailWriterController.setSubject("Fwd: " + email.getSubject());
        mailWriterController.setMessage(email.getMessage());
    }

    /**
     * replay the selected email
     */
    @FXML
    private void replyEmail() {
        Email email = mailListModel.getCurrentMail();

        root.setCenter(writer);
        mailWriterController.setReceivers(email.getSender());
        mailWriterController.setSubject("Re: " + email.getSubject());
        mailWriterController.setMessage("");
    }

    /**
     * Replay to all users the Email
     */
    @FXML
    private void replyAllEmail() {
        root.setCenter(writer);
        Email email = mailListModel.getCurrentMail();
        String username = connection.getUsername();

        StringBuilder receivers = new StringBuilder(email.getSender());
        for (String receiver : email.getReceivers()) {
            if (!Objects.equals(receiver.trim(), username)) {
                receivers.append(", ").append(receiver);
            }
        }

        mailWriterController.setReceivers(receivers.toString());
        mailWriterController.setSubject("Re: " + email.getSubject());
        mailWriterController.setMessage("");
    }

    /**
     * Initialize the Model
     * @param model
     */
    public void initModel(MailListModel model) {
        if (this.mailListModel != null) throw new IllegalStateException("Model can only be initialized once");

        this.mailListModel = model;
        model.currentMailProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                // if the new value doesn't exit, it will reset
                sender.setText("");
                receiver.setText("");
                subject.setText("");
                message.setText("");
                data.setText("");
            } else {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy, HH:mm");

                // Change the text of the label
                sender.setText(newValue.getSender());
                receiver.setText(newValue.getReceivers().toString().replace("[", "").replace("]", ""));
                subject.setText(newValue.getSubject());
                message.setText(newValue.getMessage());
                data.setText(format.format(newValue.getDate()));
            }
        });
    }
}
