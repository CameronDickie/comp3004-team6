package com.comp3004.educationmanager.decorator;

import com.comp3004.educationmanager.composite.Component;

/*
Allows a Component to be graded
 */
public class GradeableDecorator extends Decorator {
    private float grade;
    private boolean visible;

    /*
    Constructor
     */
    public GradeableDecorator(Component c) {
        super(c);
    }

    /*
    Functions from Component class
     */

    @Override
    public boolean setProperty(String property, Object value) {
        if(property.equals("grade")) {
            grade = (Float) value;
            return true;
        } else if(property.equals("visible")) {
            visible = (Boolean) value;
            return true;
        } else {
            return wrappee.setProperty(property, value);
        }
    }

    @Override
    public Object getProperty(String property) {
        if(property.equals("grade")) {
            return grade;
        } else if(property.equals("visible")) {
            return visible;
        } else {
            return wrappee.getProperty(property);
        }
    }

    @Override
    public Object executeCommand(String command, Object value) {
        return wrappee.executeCommand(command, value);
    }
}
