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
        } else if(command.equals("stringify")) {
            return Helper.objectToJSONString(this);
        } else {
            return wrappee.executeCommand(command, value);
        }
    }
}
