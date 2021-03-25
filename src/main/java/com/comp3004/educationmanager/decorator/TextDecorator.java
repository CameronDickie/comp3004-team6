package com.comp3004.educationmanager.decorator;

import com.comp3004.educationmanager.composite.Component;

public class TextDecorator extends Decorator {
    private String text;

    /*
    Constructor
     */
    public TextDecorator(Component c) {
        super(c);
    }

    /*
    Functions from Component class
     */

    @Override
    public boolean setProperty(String property, Object value) {
        if(property.equals("text")) {
            text = (String) value;
            return true;
        } else {
            return wrappee.setProperty(property, value);
        }
    }

    @Override
    public Object getProperty(String property) {
        if(property.equals("text")) {
            return text;
        } else {
            return wrappee.getProperty(property);
        }
    }

    @Override
    public void executeCommand(String command, Object value) {

    }
}
