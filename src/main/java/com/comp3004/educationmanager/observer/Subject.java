package com.comp3004.educationmanager.observer;

import java.util.ArrayList;

public abstract class Subject {
    protected ArrayList<Observer> observers = new ArrayList();

    public abstract boolean attach(Observer o);

    public abstract boolean detach(Observer o);

    public ArrayList<Observer> getObservers() { return observers; }

    public abstract void updateAll(String command, Object value);
}
