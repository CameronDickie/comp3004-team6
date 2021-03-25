package com.comp3004.educationmanager.accounts;

import com.comp3004.educationmanager.observer.Observer;

import javax.persistence.Entity;


@Entity
public class Professor extends User implements Observer {

    @Override
    public void update() {

    }
}
