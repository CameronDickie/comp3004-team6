package com.comp3004.educationmanager.decorator;

import com.comp3004.educationmanager.composite.Component;

import java.util.Calendar;

public class DeadlineDecorator extends Decorator {
    Calendar deadline = Calendar.getInstance();

    public DeadlineDecorator(Component c) {
        super(c);
    }

    @Override
    public boolean setProperty(String property, Object value) {
        return false;
    }

    @Override
    public Object getProperty(String property) {
        return null;
    }

    @Override
    public Object executeCommand(String command, Object value) {
        return null;
    }
}
