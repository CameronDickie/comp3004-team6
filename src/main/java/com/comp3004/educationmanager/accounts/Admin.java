package com.comp3004.educationmanager.accounts;

import com.comp3004.educationmanager.observer.Observer;

import javax.persistence.Entity;

/*
Class for the Admin user
Implements all admin-specific functionality, including the observer
 */
@Entity
public class Admin extends User implements Observer {

    @Override
    public void update() {

    }
}
