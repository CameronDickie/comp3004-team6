package com.comp3004.educationmanager.decorator;

import com.comp3004.educationmanager.Helper;
import com.comp3004.educationmanager.composite.Component;
import com.comp3004.educationmanager.visitor.*;

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
            file.setFile((String) value);
        } else {
            return wrappee.setProperty(property, value);
        }

        return true;
    }

    @Override
    public Object getProperty(String property) {
        if(property.equals("file")) {
            return file.getByteString();
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
        } else if(command.equals("findByPath")) {
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
