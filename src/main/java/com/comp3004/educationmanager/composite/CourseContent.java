package com.comp3004.educationmanager.composite;

import java.util.List;
import javax.persistence.*;

public class CourseContent implements Component, java.io.Serializable {
    private String name;
    private String path;

    private List<Component> children;

    /*
    Functions from Component class
     */

    @Override
    public boolean setProperty(String property, Object value) {
        if(property.equals("name")) {                                   // set name of item
            name = (String) value;
            String[] pathComponents = path.split("/");
            StringBuffer nextPath = new StringBuffer();
            for(int i = 0; i < pathComponents.length - 1; ++i) {
                nextPath.append(pathComponents[i] + "/");
            }
            setProperty("path", nextPath.toString());
            return true;
        } else if(property.equals("path")) {                            // set path of item and all children
            path = (String) value + name + "/";
            for(int i = 0; i < children.size(); ++i) {
                children.get(i).setProperty("path", path);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Object getProperty(String property) {
        if(property.equals("name")) {                                   // get name of item
            return name;
        } else if(property.equals("path")) {                            // get path of item
            return path;
        }
        else {
            return null;
        }
    }

    @Override
    public void executeCommand(String command, Object value) {
        if(command.equals("deleteAll")) {                                  // delete all children
            for(int i = 0; i < children.size(); ++i) {
                children.get(i).executeCommand("delete", value);
            }
        }
    }
}
