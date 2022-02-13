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
     * In questo metodo il client effettua la connessione al server sulla porta
     * 8195. In caso non dovesse trovarlo fa apparire un messaggio di errore.
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
     * In questo metodo il client effettua il primo caricamento dei dati.
     * Innanzitutto si identifica, in modo tale che il server possa andare a
     * recuperare le email nella cartella corretta, e successivamente si mette
     * in attesa di ricevere i contenuti, che una volta arrivati verrano
     * ordinati ed inseriti a tutti gli effetti.
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
     * In questo metodo il client effettua nuovamente il caricamento dei dati.
     * Esegue cio' quando vi sono nuove mail.
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
     * In questo metodo il client elimina la mail desiderata dalla propria lista
     * ed invia successivamente al server l'ID in modo tale che ench'esso possa
     * eliminarla a tutti gli effetti.
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
     * In questo metodo il client comunica che vuole eseguire una disconnesione.
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
     * In questo metodo il client comunica al server che e' pronto ad inviare
     * una mail e successivamente si prepara a mandargli una serie di stringhe
     * che corrispondono ai contenuti di essa.
     *
     * @param mail la mail
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
     * In questo metodo il client chiede al server quante email vi sono nella
     * cartella correlata all'utente.
     *
     * @return il numero di email sul server
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
