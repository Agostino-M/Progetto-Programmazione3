package com.prog3.clientmail.connection;

import com.prog3.clientmail.model.MailListModel;
import com.prog3.common.Email;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Connection {
    private final int port;
    private String username;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ObjectInputStream inStream;
    private ObjectOutputStream outStream;
    private MailListModel mailListModel;

    public Connection(int port, MailListModel mailListModel) {
        this.port = port;
        this.mailListModel = mailListModel;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public final String getUsername() {
        return username;
    }

    /**
     * Create connection made by the client on port 8195. If not found, an error message will appear.
     * @throws IOException
     * @throws InterruptedException
     */
    private synchronized void connect() throws IOException, InterruptedException {
        String nomeHost = InetAddress.getLocalHost().getHostName();
        socket = new Socket(nomeHost, port);
        outStream = new ObjectOutputStream(socket.getOutputStream());
        inStream = new ObjectInputStream(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.println(username);
        Thread.sleep(150);
    }

    /**
     * First load of the data.
     * The user asks for the emails in his folder and waits to recieve them.
     * When the messages will arrive, they will be sorted and added
     * @return emails
     * @throws InterruptedException
     * @throws IOException
     */
    public synchronized ArrayList<Email> loadData() throws InterruptedException, IOException {
        ArrayList<Email> emails = new ArrayList<>();
        try {
            connect();
            emails = (ArrayList<Email>) inStream.readObject();

            emails.sort((Email o1, Email o2) -> {
                if (o1.getDate() == null || o2.getDate() == null) {
                    return 0;
                }
                return o1.getDate().compareTo(o2.getDate()) > 0 ? -1 : 1;
            });
            socket.close();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Bad date format in inbox file!");
        } catch (ClassNotFoundException e) {
            System.out.println("Error in read object!");
            e.printStackTrace();
        }
        return emails;
    }

    /**
     * The client asks for the new data. This function is executed when there are new emails.
     * @return emails
     * @throws IOException
     * @throws InterruptedException
     */
    public synchronized ArrayList<Email> reloadData() throws IOException, InterruptedException {
        connect();
        out.println("reloadMails");
        ArrayList<Email> emails = new ArrayList<>();
        try {
            emails = (ArrayList<Email>) inStream.readObject();
            emails.sort((Email o1, Email o2) -> {
                if (o1.getDate() == null || o2.getDate() == null) {
                    return 0;
                }
                return o1.getDate().compareTo(o2.getDate()) > 0 ? -1 : 1;
            });
            socket.close();
        } catch (ClassNotFoundException e) {
            System.out.println("Error in read object!");
            e.printStackTrace();
        }
        return emails;
    }

    /**
     * The User ask to eliminate an email from his list and send the ID to the server to make him delete it.
     * @param id
     */
    public synchronized void deleteMail(Integer id) {
        try {
            connect();
            out.println("removeMail");
            mailListModel.removeEmail(id);
            Thread.sleep(100);
            out.println(id);
            socket.close();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * The user ask to be logged out
     */
    public synchronized void closeConnection() {
        try {
            connect();
            out.println("logout");
            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * User ask to send an email and send the mail (string series)
     * @param mail
     * @return boolean
     */
    public synchronized boolean sendMail(Email mail) {
        Boolean result = false;
        try {
            connect();
            out.println("sendMail");
            Thread.sleep(100);
            outStream.writeObject(mail);
            result = (Boolean) inStream.readObject();
            socket.close();
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Server asks the number of mail in the User's folder.
     * @return the number of mails
     * @throws IOException
     * @throws InterruptedException
     */
    public int getNumberOfMails() throws IOException, InterruptedException {
        connect();
        out.println("numberOfMails");
        Integer number = 0;
        try {
            number = (Integer) inStream.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (EOFException eofException) {
            System.out.println("EOF");
            eofException.printStackTrace();
        }

        socket.close();
        return number;
    }
}
