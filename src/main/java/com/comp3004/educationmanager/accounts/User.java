package com.comp3004.educationmanager.accounts;

public abstract class User {
    private int userID;
    private String username, password;

    public User(int userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }
}