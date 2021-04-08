package com.comp3004.educationmanager.observer;

import com.comp3004.educationmanager.accounts.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SystemData extends Subject {

    public static Admin admin;
    public static HashMap<Long, User> users = new HashMap<>();
    public static HashMap<String, CourseData> courses = new HashMap<>();

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
