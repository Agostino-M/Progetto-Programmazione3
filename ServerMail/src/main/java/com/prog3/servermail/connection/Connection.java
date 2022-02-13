package com.prog3.servermail.connection;

import com.prog3.common.Email;
import com.prog3.servermail.exception.UserException;
import com.prog3.servermail.model.FileManager;
import com.prog3.servermail.model.ServerModel;
import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Connection implements Runnable {
    private final ServerModel serverModel;
    private final Socket socket;

    private String username;
    private final ArrayList<String> usersConnected;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private PrintWriter out;


    public Connection(ServerModel serverModel, Socket socket) {
        this.serverModel = serverModel;
        this.usersConnected = serverModel.getUsersConnected();
        this.socket = socket;
        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            username = in.readLine();
            System.out.println("user: " + username);
        } catch (IOException ex) {
            Platform.runLater(() -> serverModel.addLog("Unable to read the username"));
            ex.printStackTrace();
        }

        if (!alreadyConnected()) {
            Platform.runLater(() -> serverModel.addLog(username + " has connected! Address: " + socket.getLocalAddress()));
            getMailList();
            usersConnected.add(username);
        } else {
            String operation;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                operation = in.readLine();
                try {
                    clientChoise(in, operation);
                } catch (NullPointerException e) {
                    usersConnected.remove(username);
                    socket.close();
                    e.printStackTrace();
                }
            } catch (IOException ex) {
                Platform.runLater(() -> serverModel.addLog("Unable to read the operation of " + username));
                ex.printStackTrace();
            }
        }
    }

    /**
     * Operation choice by the client (remove mail, send mail, reload mail, number of email and logout)
     * @param in bufferedReader sent from the server itself
     * @param operation operation choose by the client
     * @throws IOException
     */
    public void clientChoise(BufferedReader in, String operation) throws IOException {
        switch (operation) {
            case "removeMail": {
                try {
                    Integer id = Integer.parseInt(in.readLine());

                    FileManager.removeMessage(id, username);
                    Platform.runLater(() -> serverModel.addLog(username + " has removed the mail with id " + id));
                } catch (UserException e) {
                    Platform.runLater(() -> serverModel.addLog(username + " file not found"));
                    e.printStackTrace();
                }
                break;
            }
            case "sendMail":
                try {
                    Email email = ((Email) inputStream.readObject());
                    Platform.runLater(() -> serverModel.addLog(email.getSender() + ": message sent"));

                    Boolean success = FileManager.sendNewMessage(email);
                    if (!success) {
                        Platform.runLater(() -> serverModel.addLog("Warning: some user does not exist!"));
                    }
                    outputStream.writeObject(success);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case "reloadMails": {
                getMailList();
                break;
            }
            case "numberOfMails": {
                getNumberOfMails();
                break;
            }
            case "logout":
                Platform.runLater(() -> serverModel.addLog(username + " has disconnected!"));
                removeUser();
                break;
        }
    }

    /**
     * First load of dates. The server make a read of the User's inbox and extract all messagges that will be send to the User
     */
    public void getMailList() {
        try {
            ArrayList<Email> resultEmails = FileManager.extractMessages(username);
            resultEmails.forEach(email -> {
                email.setSubject(email.getSubject().replace("\\:", ":"));
                email.setMessage(email.getMessage().replace("\\n", "\n"));
            });

            outputStream.writeObject(resultEmails);
        } catch (UserException | IOException e) {
            Platform.runLater(() -> serverModel.addLog(username + " file not found"));
            e.printStackTrace();
        }
    }

    /**
     * Return the Numbers of mails
     */
    public void getNumberOfMails() {
        try {
            Integer number = FileManager.numberOfMails(username);
            outputStream.writeObject(number);
        } catch (UserException | IOException e) {
            Platform.runLater(() -> serverModel.addLog(username + " file not found"));
            e.printStackTrace();
        }
    }

    /**
     *  Check of the connection
     * @return
     */
    public boolean alreadyConnected() {
        return usersConnected.contains(username);
    }

    /**
     * When the user make the logout, the user is removed from the list of users connected.
     */
    public void removeUser() {
        usersConnected.remove(username);
    }
}
