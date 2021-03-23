package com.comp3004.educationmanager.observer;

import java.util.List;

public abstract class Subject {
    protected List<Observer> observers;

    public boolean attach(Observer o) {
        return observers.add(o);
    }

    public boolean detach(Observer o) {
        return observers.remove(o);
    }

    public abstract void updateAll();
}