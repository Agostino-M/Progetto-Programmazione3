package com.prog3.clientmail.controller;

import com.prog3.clientmail.connection.Connection;
import com.prog3.clientmail.model.MailListModel;
import com.prog3.common.Email;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.SocketException;
import java.text.SimpleDateFormat;

public class MenuController {
    @FXML
    private ListView<Email> listView;
    private Connection connection;
    private BorderPane root;
    private Node writer;
    private Node reader;
    private MailListModel mailListModel;
    private MailWriterController mailWriterController;

    public void setMailWriterController(MailWriterController mailWriterController) {
        this.mailWriterController = mailWriterController;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Initializes the model
     *
     * @param mailListModel
     */
    public void initModel(MailListModel mailListModel) {
        if (this.mailListModel != null)
            throw new IllegalStateException("Model can only be initialized once");

        this.mailListModel = mailListModel;
        listView.setItems(mailListModel.getEmailList());

        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            mailListModel.setCurrentMail(newValue);
            if (newValue != null)
                root.setCenter(reader);
        });

        listView.setCellFactory(mailListView -> new ListCell<>() {
            @Override
            public void updateItem(Email email, boolean empty) {
                super.updateItem(email, empty);
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                if (empty) {
                    setText(null);
                } else {
                    setText(format.format(email.getDate()) + "\n" + email.getSender() + "\n" + email.getSubject());
                }
            }
        });

        new Thread(() -> {
            while (true) {
                try {
                    int emailsListView = mailListModel.getEmailList().size();
                    int emailsServerFile = connection.getNumberOfMails();

                    if (emailsListView != emailsServerFile) {
                        Platform.runLater(() -> {
                            try {
                                mailListModel.loadData(connection.reloadData());
                            } catch (IOException e) {
                                Platform.runLater(() -> {
                                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Errore, server non disponibile!");
                                    Stage owner = (Stage) listView.getScene().getWindow();
                                    errorAlert.initModality(Modality.APPLICATION_MODAL);
                                    errorAlert.initOwner(owner);
                                    errorAlert.showAndWait();

                                });
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    if (emailsServerFile > emailsListView) {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Hai un nuovo messaggio!");
                            Stage owner = (Stage) listView.getScene().getWindow();
                            alert.initModality(Modality.APPLICATION_MODAL);
                            alert.initOwner(owner);
                            alert.getDialogPane().setHeaderText("Nuovo messaggio");
                            alert.setTitle("Nuovo messaggio");
                            alert.showAndWait();
                        });
                    }

                    Thread.sleep(5000);
                } catch (SocketException se) {
                    try {
                        mailListModel.loadData(connection.loadData());
                    } catch (IOException | InterruptedException e) {
                        Platform.runLater(() -> {
                            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Errore, server non disponibile!");
                            Stage owner = (Stage) listView.getScene().getWindow();
                            errorAlert.initModality(Modality.APPLICATION_MODAL);
                            errorAlert.initOwner(owner);
                            errorAlert.showAndWait();

                        });
                        e.printStackTrace();
                    } finally {
                        try {
                            Thread.sleep(20000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException ex) {
                    System.out.println("IOException");
                    ex.printStackTrace();
                } catch (InterruptedException ex) {
                    System.out.println("InterruptedException ERROR!");
                }
            }
        }).start();
    }

    public void setScene(BorderPane root, Node writer, Node reader) {
        this.root = root;
        this.writer = writer;
        this.reader = reader;
    }

    /**
     * refresh the mail
     */
    @FXML
    private void refresh() {
        try {
            mailListModel.loadData(connection.reloadData());
        } catch (IOException e) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Errore nel caricamento dei messaggi!");
                Stage owner = (Stage) listView.getScene().getWindow();
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.initOwner(owner);
                alert.showAndWait();
            });
            e.printStackTrace();
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
    }

    /**
     * Show the new message model
     */
    @FXML
    private void newMessage() {
        mailWriterController.setSubject("");
        mailWriterController.setReceivers("");
        mailWriterController.setMessage("");
        root.setCenter(writer);
    }
}
