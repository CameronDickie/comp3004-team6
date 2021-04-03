package com.comp3004.educationmanager.accounts;

import javax.persistence.Entity;

/*
Class for the Admin user
Implements all admin-specific functionality, including the observer
 */
@Entity
public class Admin extends User {

    @Override
    public void update(String command, Object value) {

    }
}
