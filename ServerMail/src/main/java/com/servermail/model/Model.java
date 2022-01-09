package com.example.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Model {
    HashMap<String, Account> accounts;
    Account currentUser;

    public Model(Account currentUser) {
        this.accounts = new HashMap<>();
        this.currentUser = currentUser;
    }

    public void setUpServer() throws Exception {
        // Extracting all the accounts
        ArrayList<Account> accountList = FileManager.extractUsers();
        if (accountList == null)
            throw new Exception("Error");

        for (Account account : accountList)
            accounts.put(account.getEmail(), account);

        // Extracting all the messages
        currentUser.setInboxMessages(FileManager.extractMessages(currentUser.getEmail()));
    }
}
