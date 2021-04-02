package com.comp3004.educationmanager.observer;

import java.util.ArrayList;

public abstract class Subject {
    protected ArrayList<Observer> observers = new ArrayList();

    public boolean attach(Observer o) {
        return observers.add(o);
    }

    public boolean detach(Observer o) {
        return observers.remove(o);
    }

    public abstract void updateAll(String command, Object value);
}
