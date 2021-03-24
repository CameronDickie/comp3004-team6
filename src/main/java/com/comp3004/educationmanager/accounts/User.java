package com.comp3004.educationmanager.accounts;

public abstract class User {
    protected int userID;
    protected String username, password;
    protected static int nextUID = 1;
    public User(String username, String password) {
        this.userID = nextUID;
        this.username = username;
        this.password = password;
        this.nextUID +=1;
    }

    public String getName() {
        return this.username;
    }
    public int getUserId() {
        return this.userID;
    }
    public String getPassword() {
        return this.password;
    }
}