package com.comp3004.educationmanager.observer;

import com.comp3004.educationmanager.accounts.*;

import java.util.List;

public class SystemData extends Subject {
    List<Student> students;
    List<Professor> professors;

    public void updateAll() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
