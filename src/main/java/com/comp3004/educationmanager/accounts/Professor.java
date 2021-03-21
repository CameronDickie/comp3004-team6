package com.comp3004.educationmanager.accounts;

import com.comp3004.educationmanager.observer.Observer;

public class Professor extends User implements Observer {
    public Professor(String username, String password) {
        super(username, password);
    }

    @Override
    public void update() {

    }
}
