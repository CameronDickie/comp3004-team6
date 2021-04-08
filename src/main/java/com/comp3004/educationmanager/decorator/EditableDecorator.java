package com.comp3004.educationmanager.decorator;

import com.comp3004.educationmanager.Helper;
import com.comp3004.educationmanager.composite.Component;

public class EditableDecorator extends Decorator {
    private boolean editable;

    /*
    Constructor
     */
    public EditableDecorator(Component c) {
        super(c);
        editable = true;
    }

    /*
    Functions from Component class
     */

    @Override
    public boolean setProperty(String property, Object value) {
        if(property.equals("editable")) {
            editable = (Boolean) value;
            return true;
        } else {
            return wrappee.setProperty(property, value);
        }
    }

    @Override
    public Object getProperty(String property) {
        if(property.equals("editable")) {
            return editable;
        } else {
            return wrappee.getProperty(property);
        }
    }

    @Override
    public Object executeCommand(String command, Object value) {
        System.out.println("Editable: " + getProperty("fullPath") + " | " + command + " | " + value);
        // System.out.println("Executing command (editable): " + command);
        if(command.equals("findByPath")) {
            Component c = (Component) wrappee.executeCommand(command, value);
            if(c != null) {
                System.out.println("EDITABLE: " + c.getProperty("fullPath") + " | " + getProperty("fullPath"));
                if(((String) c.getProperty("fullPath")).equals((String) getProperty("fullPath"))) {
                    System.out.println("returning decorator");
                    return this;
                } else if(((String) c.getProperty("fullPath")).equals((String) value)) {
                    System.out.println("returning wrappee return");
                    return c;
                }
            }
            return null;
        } else if(command.equals("stringify")) {
            return Helper.objectToJSONString(this);
        } else {
            return wrappee.executeCommand(command, value);
        }
    }
}
