package com.comp3004.educationmanager.composite;

import java.util.ArrayList;
import java.util.List;

/*
The Composite object - stores multiple Component objects
Implements the Component functions
 */
public class CourseContent implements Component, java.io.Serializable {
    private String name;
    private String path;
    private boolean visible;

    private ArrayList<Component> children = new ArrayList();

    /*
    Constructor
     */
    public CourseContent(String name, String path) {
        this.name = name;
        this.path = path;
        this.visible = true;
    }

    public CourseContent(String name, String path, boolean visible) {
        this.name = name;
        this.path = path;
        this.visible = visible;
    }

    /*
    Functions from Component class
     */

    @Override
    public boolean setProperty(String property, Object value) {
        if(property.equals("name")) {                                   // set name of item
            name = (String) value;
            setProperty("path", path);
            return true;
        } else if(property.equals("path")) {                            // set path of item and all children
            path = (String) value;
            for(int i = 0; i < children.size(); ++i) {
                children.get(i).setProperty("path", getProperty("fullPath"));
            }
            return true;
        } else if(property.equals("visible")) {
            visible = (boolean) value;
            for(int i = 0; i < children.size(); ++i) {
                children.get(i).setProperty("visible", visible);
            }
            return true;
        } else {
            System.out.println("Property " + property + " not found.");
            return false;
        }
    }

    @Override
    public Object getProperty(String property) {
        if(property.equals("name")) {                                   // get name of item
            return name;
        } else if(property.equals("path")) {                            // get path of item
            return path;
        } else if (property.equals("fullPath")) {                       // get full path (including name)
            return path + name + "/";
        } else if(property.equals("visible")) {                         // get visible property (visible to students)
            return visible;
        } else {
            System.out.println("Property " + property + " not found.");
            return null;
        }
    }

    @Override
    public Object executeCommand(String command, Object value) {
        if(command.equals("delete")) {                                  // delete all children
            for (int i = 0; i < children.size(); ++i) {
                children.get(i).executeCommand(command, value);
            }
        } else if(command.equals("findByPath")) {                       // find an item by it's path
            System.out.println("findByPath: " + (String) getProperty("fullPath") + " | " + (String) value);
            if(((String) getProperty("fullPath")).equals((String) value)) {
                return this;
            } else {
                for(int i = 0; i < children.size(); ++i) {
                    Component c = (Component) children.get(i).executeCommand(command, value);
                    if(c != null) {
                        return c;
                    }
                }
            }
        } else if(command.equals("addItem")) {                          // add an item as a descendant (direct child or child of child...)
            Component c = (Component) value;
            String cPath = (String) c.getProperty("path");
            if(cPath.equals((String) getProperty("fullPath"))) {
                children.add(c);
            } else {
                for(int i = 0; i < children.size(); ++i) {
                    children.get(i).executeCommand(command, value);
                }
            }
        } else {
            System.out.println("Command " + command + " not found.");
        }

        return null;
    }
}
