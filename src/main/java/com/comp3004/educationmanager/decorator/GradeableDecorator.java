package com.comp3004.educationmanager.decorator;

import com.comp3004.educationmanager.Helper;
import com.comp3004.educationmanager.composite.Component;

/*
Allows a Component to be graded
 */
public class GradeableDecorator extends Decorator {
    private float grade;

    /*
    Constructor
     */
    public GradeableDecorator(Component c) {
        super(c);
        grade = -1;
    }

    /*
    Functions from Component class
     */

    @Override
    public boolean setProperty(String property, Object value) {
        if(property.equals("grade")) {
            grade = (Float) value;
            return true;
        } else {
            return wrappee.setProperty(property, value);
        }
    }

    @Override
    public Object getProperty(String property) {
        if(property.equals("grade")) {
            return grade;
        } else {
            return wrappee.getProperty(property);
        }
    }

    @Override
    public Object executeCommand(String command, Object value) {
        if(command.equals("findByPath")) {
            Component c = (Component) wrappee.executeCommand(command, value);
            if(c != null) {
                if(((String) c.getProperty("fullPath")).equals((String) getProperty("fullPath"))) {
                    return this;
                } else if(((String) c.getProperty("fullPath")).equals((String) value)) {
                    return c;
                }
            }
            return null;
        }  else if(command.equals("stringify")) {
            return Helper.objectToJSONString(this);
        } else {
            return wrappee.executeCommand(command, value);
        }
    }
}
