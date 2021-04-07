package com.comp3004.educationmanager.observer;

import com.comp3004.educationmanager.accounts.*;

import java.util.ArrayList;
import java.util.List;

public class SystemData extends Subject {
    ArrayList<Student> allStudents = new ArrayList<>();
    ArrayList<Professor> allProfessors = new ArrayList<>();

    @Override
    public boolean attach(Observer o) {
        return observers.add(o);
    }

    @Override
    public boolean detach(Observer o) {
        return observers.remove(o);
    }


    public void updateAll(String command, Object value) {
        for (Observer observer : observers) {
            observer.update(command, value);
        }
    }
}
