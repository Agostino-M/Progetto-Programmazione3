package com.prog3.servermail.model;


import com.prog3.common.Email;
import com.prog3.servermail.exception.UserException;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class FileManager {
    private static final String usersDirPath = "src/main/files/";

    /**
     * @return ID
     */
    public synchronized static int firstAvailableId(String email) throws UserException, IOException {
        File userMessage = new File(usersDirPath + email + "/inbox.txt");
        if (!userMessage.exists()) throw new UserException(email + " file does not exists!");

        if (userMessage.length() == 0) return 1;

        BufferedReader reader = new BufferedReader(new FileReader(userMessage));
        int id = 0;
        String line = "";
        while ((line = reader.readLine()) != null) {
            String[] split = line.split("(?<!\\\\):");
            id = Integer.parseInt(split[0]);
        }
        return id + 1;
    }

    /**
     * @return
     */
    public synchronized static int numberOfMails(String email) throws UserException, IOException {
        File userMessage = new File(usersDirPath + email + "/inbox.txt");
        if (!userMessage.exists()) throw new UserException(email + " file does not exists!");

        if (userMessage.length() == 0) return 0;

        BufferedReader reader = new BufferedReader(new FileReader(userMessage));
        int count = 0;
        while (reader.readLine() != null) {
            count++;
        }
        return count;
    }

    /**
     * @param email
     * @return
     * @throws Exception
     * @throws UserException
     */
    public synchronized static ArrayList<Email> extractMessages(String email) throws UserException, IOException {
        ArrayList<Email> userEmails = new ArrayList<>();

        File userMessage = new File(usersDirPath + email + "/inbox.txt");
        if (!userMessage.exists()) throw new UserException("User file does not exists!");

        if (userMessage.length() == 0) return new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(userMessage));

        try {
            Email currentEmail;
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split("(?<!\\\\):");
                currentEmail = new Email();
                currentEmail.setId(Integer.parseInt(split[0]));
                currentEmail.setSender(split[1]);
                currentEmail.setSubject(split[2]);

                String[] destSplit = split[3].replace("[", "")
                        .replace("]", "")
                        .replace("  ", " ")
                        .split(",");

                currentEmail.setReceivers(new ArrayList<>(Arrays.asList(destSplit)));
                currentEmail.setMessage(split[4]);

                DateFormat format = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
                Date date = format.parse(split[5].replace("\\:", ":"));
                currentEmail.setDate(date);
                userEmails.add(currentEmail);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Bad format in file " + email + "/inbox.txt");
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("Bad date in file " + email + "/inbox.txt");
            e.printStackTrace();
        }

        reader.close();
        return userEmails;
    }

    public synchronized static boolean sendNewMessage(Email email) throws IOException {
        for (String receiver : email.getReceivers()) {
            receiver = receiver.trim();
            int newId;
            try {
                newId = FileManager.firstAvailableId(receiver);
                email.setId(newId);
                createMessage(email, receiver);
            } catch (UserException e) {
                return false;
            }
        }
        return true;
    }

    private synchronized static void createMessage(Email email, String username) throws IOException, UserException {
        File userMessage = new File(usersDirPath + username + "/inbox.txt");

        if (!userMessage.exists()) throw new UserException(username + " file does not exists!");

        BufferedWriter writer = new BufferedWriter(new FileWriter(userMessage, true));
        PrintWriter pw = new PrintWriter(writer);
        pw.println(email);

        System.out.println("Data Successfully appended into file");
        pw.flush();

        writer.close();
        pw.close();
    }

    /**
     * Remove a message
     *
     * @param id
     * @param username
     * @return
     * @throws UserException
     * @throws IOException
     */
    public synchronized static boolean removeMessage(Integer id, String username) throws UserException, IOException {
        File userMessage = new File(usersDirPath + username + "/inbox.txt");

        if (!userMessage.exists()) throw new UserException("User file does not exists!");

        if (userMessage.length() == 0) return true;

        File tempFile = new File(usersDirPath + username + "/_inbox.txt");

        BufferedReader reader = new BufferedReader(new FileReader(userMessage));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String currentEmail;
        while ((currentEmail = reader.readLine()) != null) {
            String[] split = currentEmail.split("(?<!\\\\):");
            if (Integer.parseInt(split[0]) == id) continue;
            writer.write(currentEmail + System.getProperty("line.separator"));
        }

        writer.close();
        reader.close();
        return tempFile.renameTo(userMessage);
    }
}
