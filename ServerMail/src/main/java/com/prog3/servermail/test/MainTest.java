package com.prog3.servermail.test;

import com.prog3.common.Email;
import com.prog3.servermail.exception.UserException;
import com.prog3.servermail.model.FileManager;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class MainTest {
    public static void main(String[] args) {
        try {
            System.out.println(FileManager.numberOfMails("agostino"));
            System.out.println(FileManager.firstAvailableId("agostino"));

        } catch (UserException | IOException e) {
            e.printStackTrace();
        }


//        int port = 8195;
//        try {
//            String nomeHost = InetAddress.getLocalHost().getHostName();
//            Socket s = new Socket(nomeHost, port);
//            try {

//                ObjectOutputStream outStream = new ObjectOutputStream(s.getOutputStream());
//                ObjectInputStream inStream = new ObjectInputStream(s.getInputStream());
//
//                PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);
//                out.println("mailList");
//                Thread.sleep(50);
//                out.println("ago");
//
//                System.out.println((ArrayList<Email>) inStream.readObject());
//

//                out.println("sendMail");
//                Thread.sleep(500);
//
//                ArrayList<String> receivers = new ArrayList<>();
//                receivers.add("ago");
//                receivers.add("andrea");
//                Email email = new Email("aldo", receivers, "Oggettot", "Messaggiog", "21/10/2022");
//                outStream.writeObject(email);
//                System.out.println("stampato email");

//                out.println("removeMail");
//                Thread.sleep(500);
//                out.println("ago");
//                out.println("1");

//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            } finally {
//                s.close();
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }
}
