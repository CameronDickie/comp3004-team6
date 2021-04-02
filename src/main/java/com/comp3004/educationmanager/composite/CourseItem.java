package com.comp3004.educationmanager.composite;

/*
A single course item
Implements the Component functions
 */
public class CourseItem implements Component, java.io.Serializable {
    private String name;
    private String path;
    private boolean visible;

    /*
    Constructor
     */
    public CourseItem(String name, String path) {
        this.name = name;
        this.path = path;
    }
    /*
    Functions from Component class
     */

    @Override
    public boolean setProperty(String property, Object value) {
        if(property.equals("name")) {                                   // set name of item
            name = (String) value;
            return true;
        } else if(property.equals("path")) {                            // set path of item
            path = (String) value;
            return true;
        } else if(property.equals("visible")) {
            visible = (boolean) value;
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
        } else if(property.equals("fullPath")) {                        // get path and name of item
            return path + name + "/";
        } else if(property.equals("visible")) {
            return visible;
        } else {
            System.out.println("Property " + property + " not found.");
            return null;
        }
    }

    @Override
    public Object executeCommand(String command, Object value) {
        if(command.equals("delete")) {                                  // delete this object (does nothing right now)

        } else if(command.equals("findByPath")) {
            if(((String) getProperty("fullPath")).equals((String) value)) {
                return this;
            }
        } else {
            System.out.println("Command " + command + " not found.");
        }

        return null;
    }
}
