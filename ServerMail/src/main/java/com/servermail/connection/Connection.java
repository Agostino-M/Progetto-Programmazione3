package com.servermail.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection {

    Socket socket = null;
    ObjectInputStream inStream = null;
    ObjectOutputStream outStream = null;

    /**
     * Il server si mette in ascolto su una determinata porta e serve i client.
     * I messaggi ricevuti dai client vengono stampati a video, modificati e inviati
     * nuovamente al mittente.
     * <p>
     * NB: Questa implementazione del server usa un solo thread, NON è un'implementazione
     * scalabile, NON è sufficiente ai fini del progetto.
     *
     *  port la porta su cui è in ascolto il server.
     */
//    public void listen(int port) {
//        try {
//            ServerSocket serverSocket = new ServerSocket(port);
//
//            while (true) {
//                serveClient(serverSocket);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (socket != null)
//                try {
//                    socket.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//        }
//    }

//    // Gestisce un singolo client
//    private void serveClient(ServerSocket serverSocket) {
//        try {
//            openStreams(serverSocket);
//
//            List<Student> students = (List<Student>) inStream.readObject();
//
//            updateStudents(students);
//
//            outStream.writeObject(students);
//            outStream.flush();
//
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        } finally {
//            closeStreams();
//        }
//    }

    // Chiude gli stream utilizzati durante l'ultima connessione
    private void closeStreams() {
        try {
            if (inStream != null) {
                inStream.close();
            }

            if (outStream != null) {
                outStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Aggiorna i dati studente passati in input
//    private void updateStudents(List<Student> students) {
//        if (students != null && students.size() > 0) {
//            for (Student s : students) {
//                System.out.println("Oggetto ricevuto => " + s.toString());
//                s.setDescription(s.getDescription() + " PROMOSSO!");
//            }
//        }
//    }

    // apre gli stream necessari alla connessione corrente
    private void openStreams(ServerSocket serverSocket) throws IOException {
        socket = serverSocket.accept();
        System.out.println("Server Connesso");

        inStream = new ObjectInputStream(socket.getInputStream());
        outStream = new ObjectOutputStream(socket.getOutputStream());
        outStream.flush();
    }
}

///**
// @author Cay Horstmann
// @version 1.20 2004-08-03
// modificata...
// */
//
//import java.io.*;
//        import java.net.*;
//        import java.util.*;
//
///**
// This program implements a multithreaded server that listens to port 8189 and echoes back
// all client input.
// */
//public class ThreadedEchoServer {
//    public static void main(String[] args ) {
//        System.out.println("Finestra del server: ");
//        try {
//            int i = 1;
//            ServerSocket s = new ServerSocket(8189);
//
//            while (true) {
//                Socket incoming = s.accept(); // si mette in attesa di richiesta di connessione e la apre
//                System.out.println("Spawning " + i);
//                Runnable r = new ThreadedEchoHandler(incoming, i);
//                new Thread(r).start();
//                i++;
//            }
//        }
//        catch (IOException e) {e.printStackTrace();}
//    }
//}
//
///**
// This class handles the client input for one server socket connection.
// */
//class ThreadedEchoHandler implements Runnable {
//
//    private Socket incoming;
//    private int counter;
//
//    /**
//     Constructs a handler.
//     @param i the incoming socket
//     @param c the counter for the handlers (used in prompts)
//     */
//    public ThreadedEchoHandler(Socket in, int c) {
//        incoming = in;
//        counter = c;
//    }
//
//    public void run() {
//        try {
//            try {
//                InputStream inStream = incoming.getInputStream();
//                OutputStream outStream = incoming.getOutputStream();
//
//                Scanner in = new Scanner(inStream);
//                PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);
//
//                out.println( "Hello! Enter BYE to exit." );
//
//                // echo client input
//                boolean done = false;
//                while (!done && in.hasNextLine()) {
//                    String line = in.nextLine();
//                    out.println("Echo: " + line);
//                    System.out.println("ECHO: "+ line);
//                    if (line.trim().equals("BYE"))
//                        done = true;
//                }
//            }
//            finally {
//                incoming.close();
//            }
//        }
//        catch (IOException e) {e.printStackTrace();}
//    }
//
//}
//
