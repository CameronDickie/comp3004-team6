package com.comp3004.educationmanager.accounts;

import com.comp3004.educationmanager.observer.Observer;

public class Admin extends User implements Observer {

    public Admin() {
        super("admin", "pass");
        System.out.println("admin made...");
    }
    public Admin(String username, String password) {
        super(username, password);
        System.out.println("admin made...");
    }

    @Override
    public void update() {

    }
}
