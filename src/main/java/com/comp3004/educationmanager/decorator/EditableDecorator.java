package com.comp3004.educationmanager.decorator;

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
        return wrappee.executeCommand(command, value);
    }
}
