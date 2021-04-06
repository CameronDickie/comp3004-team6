package com.comp3004.educationmanager.decorator;

import com.comp3004.educationmanager.composite.Component;
import com.comp3004.educationmanager.visitor.*;
import org.apache.commons.io.FileUtils;

public class FileDecorator extends Decorator {
    private FileInterface file;

    /*
    Constructor
     */
    public FileDecorator(Component c) {
        super(c);
        setFileType((String) c.getProperty("type"));
    }

    /*
    Functions from Component class
     */

    @Override
    public boolean setProperty(String property, Object value) {
        if(property.equals("file")) {
            file.setFile((byte[]) value);
        } else {
            return wrappee.setProperty(property, value);
        }

        return true;
    }

    @Override
    public Object getProperty(String property) {
        if(property.equals("file")) {
            return file.getBytes();
        } else {
            return wrappee.getProperty(property);
        }
    }

    @Override
    public Object executeCommand(String command, Object value) {
        if(command.equals("download")) {
            return file.accept(new FileDownloadVisitor());
        } else if(command.equals("viewAsPDF")) {
            return file.accept(new FileViewVisitor());
        } else {
            return wrappee.executeCommand(command, value);
        }
    }

    private void setFileType(String type) {
        if(type.equals("PDF")) {
            file = new PDF();
        } else if(type.equals("PPTX")) {
            file = new PPTX();
        } else if(type.equals("DOCX")) {
            file = new DOCX();
        }
    }
}
