package com.comp3004.educationmanager.accounts;

import com.comp3004.educationmanager.observer.Observer;

public abstract class User implements Observer {
    protected int userID;
    protected String username, password;
    protected static int nextUID = 0;

    public User(String username, String password) {
        this.userID = nextUID;
        this.username = username;
        this.password = password;
        this.nextUID +=1;
    }
}