package com.comp3004.educationmanager.composite;

import com.comp3004.educationmanager.Helper;

/*
A single course item
Implements the Component functions
 */
public class CourseItem implements Component, java.io.Serializable {
    private String name;
    private String path;
    private String type;
    private long userID;
    private String userType;
    private boolean visible;

    /*
    Constructor
     */
    public CourseItem(String name, String path, String type, long userID, String userType, boolean visible) {
        this.name = name;
        this.path = path;
        this.type = type;
        this.userID = userID;
        this.userType = userType;
        this.visible = visible;
    }

    /*
    Functions from Component class
     */

    @Override
    public boolean setProperty(String property, Object value) {
        if(property.equals("name")) {                                   // set name of item
            name = (String) value;;
        } else if(property.equals("path")) {                            // set path of item
            path = (String) value;
        } else if(property.equals("visible")) {
            visible = (boolean) value;
        } else if(property.equals("type")) {
            type = (String) value;
        } else if(property.equals("userID")) {
            userID = (long) value;
        } else if(property.equals("userType")) {
            userType = (String) value;
        } else {
            System.out.println("Property " + property + " not found.");
            return false;
        }

        return true;
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
        } else if(property.equals("type")) {
            return type;
        } else {
            System.out.println("Property " + property + " not found.");
            return null;
        }
    }

    @Override
    public Object executeCommand(String command, Object value) {
        if(command.equals("delete")) {                                  // delete this object (does nothing right now)
            return true;
        } else if(command.equals("findByPath")) {
            if(((String) getProperty("fullPath")).equals((String) value)) {
                return this;
            }
            return null;
        } else if(command.equals("stringify")) {
            return Helper.objectToJSONString(this);
        } else {
            System.out.println("Command " + command + " not found.");
            return null;
        }
    }
}
