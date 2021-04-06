package com.comp3004.educationmanager.decorator;

import com.comp3004.educationmanager.composite.Component;
import com.comp3004.educationmanager.visitor.FileDownloadVisitor;
import com.comp3004.educationmanager.visitor.FileInterface;
import com.comp3004.educationmanager.visitor.FileViewVisitor;
import org.apache.commons.io.FileUtils;

public class FileDecorator extends Decorator {
    private FileInterface file;

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
            file.setFile((byte[]) value);
        } else {
            return wrappee.setProperty(property, value);
        }

        return true;
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
            return file.accept(new FileDownloadVisitor());
        } else if(command.equals("viewAsPDF")) {
            return file.accept(new FileViewVisitor());
        } else {
            return wrappee.executeCommand(command, value);
        }
    }
}
