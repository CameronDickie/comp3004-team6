package com.comp3004.educationmanager.decorator;

import com.comp3004.educationmanager.composite.Component;
import org.apache.commons.io.FileUtils;

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
            try {
                byte[] fileBytes = (byte[]) value;
                FileUtils.writeByteArrayToFile(file, fileBytes);
                return true;
            } catch(java.io.IOException e) {
                System.out.println("Error reading file from bytes:  " + wrappee.getProperty("name"));
                return false;
            }
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
    public Object executeCommand(String command, Object value) {
        if(command.equals("download")) {
            return downloadFile();
        } else if(command.equals("viewAsPDF")) {
            return viewFile();
        } else {
            return wrappee.executeCommand(command, value);
        }
    }

    public byte[] downloadFile() {
        try {
            return FileUtils.readFileToByteArray(file);
        } catch(java.io.IOException e) {
            System.out.println("Error downloading file: " + wrappee.getProperty("name"));
            return null;
        }
    }

    public byte[] viewFile() {
        try {
            // TODO: convert file to PDF before returning byte array
            return FileUtils.readFileToByteArray(file);
        } catch(java.io.IOException e) {
            System.out.println("Error viewing file as PDF: " + wrappee.getProperty("name"));
            return null;
        }
    }
}
