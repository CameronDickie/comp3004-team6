package com.comp3004.educationmanager.accounts;

import com.comp3004.educationmanager.observer.Observer;

import javax.persistence.Entity;

/*
Class for the Student user
Implements all student-specific functionality, including the observer
 */
@Entity
public class Student extends User implements Observer{

    @Override
    public void update() {

    }
}
