package com.comp3004.educationmanager.accounts;

import com.comp3004.educationmanager.observer.Observer;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="students")
public class Student extends User implements Observer {

    public Student() { super ("", "");}
    public Student(String username, String password) {
        super(username, password);
    }

    @Override
    public void update() {

    }
}
