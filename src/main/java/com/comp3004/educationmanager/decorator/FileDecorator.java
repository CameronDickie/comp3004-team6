package com.comp3004.educationmanager.decorator;

import com.comp3004.educationmanager.composite.Component;

import java.io.File;

public class FileDecorator extends Decorator {
    private File file;

    /*
    Constructor
     */
    public FileDecorator(Component c) {
        super(c);
    }

    /*
    Functions from Component class
     */

    @Override
    public boolean setProperty(String property, Object value) {
        if(property.equals("file")) {
            file = (File) value;
            return true;
        } else {
            return wrappee.setProperty(property, value);
        }
    }

    @Override
    public Object getProperty(String property) {
        if(property.equals("file")) {
            return file;
        } else {
            return wrappee.getProperty(property);
        }
    }

    @Override
    public void executeCommand(String command, Object value) {
        if(command.equals("download")) {
            downloadFile();
        } else {
            wrappee.executeCommand(command, value);
        }
    }

    public void downloadFile() {
        // code to download file goes here (possibly using visitor pattern?)
    }
}
