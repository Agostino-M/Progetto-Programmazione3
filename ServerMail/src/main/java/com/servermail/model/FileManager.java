package com.example.model;

import com.example.comunication.Email;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class FileManager {
    private static final String usersFilePath = "../../files/users.txt";
    private static final String usersDirPath = "../../files/";

    public FileManager() {
    }

    public static void createNewUser(String username, String password) throws IOException {
        if (username == null || password == null)
            throw new IllegalArgumentException("Argument!!!!!");

        File usersFile = new File(usersFilePath);
        BufferedWriter bufferedWriter = new BufferedWriter(new PrintWriter(usersFile));

        // Writing new user in the end of the file
        bufferedWriter.append(username).append(":").append(password).append("\n").flush();

        // Creating user's directory
        Files.createDirectory(Paths.get(usersDirPath + username));

        // Creating inbox file in user's directory
        File newInboxFile = new File(usersDirPath + username + "/inbox.txt");

        bufferedWriter.close();
    }

    public static ArrayList<Account> extractUsers() throws IOException {
        File users = new File(usersFilePath);
        String line = "";
        ArrayList<Account> userList = new ArrayList<>();

        if (users.length() == 0)
            return null;

        BufferedReader reader = new BufferedReader(new FileReader(users));

        while ((line = reader.readLine()) != null) {
            String[] lineSplitted = line.split(":");
            userList.add(new Account(lineSplitted[0], lineSplitted[1]));
        }

        reader.close();
        return userList;
    }

    public static ArrayList<Email> extractMessages(String email) throws Exception {
        File userDirectory = new File(usersDirPath + email);
        String line = "";
        ArrayList<Email> userEmails = new ArrayList<>();

        if (!userDirectory.exists() && userDirectory.isDirectory())
            throw new Exception();

        File userMessage = new File(usersDirPath + email + "/inbox.txt");
        if (!userMessage.exists())
            throw new Exception();

        if (userMessage.length() == 0)
            return null;

        BufferedReader reader = new BufferedReader(new FileReader(userMessage));
        Email currentEmail;
        while ((line = reader.readLine()) != null) {
            String[] split = line.split(":");
            currentEmail = new Email();
            currentEmail.setSender(split[0]);
            currentEmail.setSubject(split[1]);

            String[] destSplit = split[2].replaceAll("[<>]", "").split(",");

            currentEmail.setReceivers(new ArrayList<>(Arrays.asList(destSplit)));
            currentEmail.setMessage(split[3]);
            userEmails.add(currentEmail);
        }

        reader.close();
        return userEmails;
    }
}
