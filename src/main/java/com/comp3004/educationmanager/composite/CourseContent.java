package com.comp3004.educationmanager.composite;

import com.comp3004.educationmanager.Helper;

import java.util.ArrayList;

/*
The Composite object - stores multiple Component objects
Implements the Component functions
 */
public class CourseContent implements Component, java.io.Serializable {
    private String name;
    private String path;
    private String type;
    private long userID;
    private String userType;
    private boolean visible;

    private ArrayList<Component> children = new ArrayList();

    /*
    Constructor
     */
    public CourseContent(String name, String path, String type, long userID, String userType, boolean visible) {
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
            name = (String) value;
            setProperty("path", path);
        } else if(property.equals("path")) {                            // set path of item and all children
            path = (String) value;
            for(int i = 0; i < children.size(); ++i) {
                children.get(i).setProperty("path", getProperty("fullPath"));
            }
        } else if(property.equals("visible")) {
            visible = (boolean) value;
            for(int i = 0; i < children.size(); ++i) {
                children.get(i).setProperty("visible", visible);
            }
        } else if(property.equals("type")) {
            type = (String) value;
        } else if(property.equals("userID")) {
            userID = (long) value;
        } else if(property.equals("userType")) {
            userType = (String) value;
        } else {
            boolean shouldReturn = false;
            for(int i = 0; i < children.size(); ++i) {
                shouldReturn = children.get(i).setProperty(property, value);
                if(shouldReturn) break;
            }
            return shouldReturn;
        }

        return true;
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
        } else if(property.equals("type")) {
            return type;
        } else {
            System.out.println("Property " + property + " not found.");
            return null;
        }
    }

    @Override
    public Object executeCommand(String command, Object value) {
        if(command.equals("delete-item")) {                                  // delete all children
            for (int i = 0; i < children.size(); ++i) {
                if(((String) children.get(i).getProperty("fullPath")).equals((String) value)) {
                    children.remove(i);
                    return true;
                }
                children.get(i).executeCommand(command, value);
            }
            return false;
        } else if(command.equals("findByPath")) {                       // find an item by it's path
            if(((String) getProperty("fullPath")).equals((String) value)) {
                System.out.println("\t-returning this");
                return this;
            } else {
                for(int i = 0; i < children.size(); ++i) {
                    System.out.print("Child: ");
                    Component c = (Component) (children.get(i).executeCommand(command, value));
                    if(c != null) return c;
                }
            }
            return null;
        } else if(command.equals("addItem")) {                          // add an item as a descendant (direct child or child of child...)
            Component c = (Component) value;
            String cPath = (String) c.getProperty("path");
            if (cPath.equals((String) getProperty("fullPath"))) {
                children.add(c);
            } else {
                for (int i = 0; i < children.size(); ++i) {
                    children.get(i).executeCommand(command, value);
                }
            }
            return true;
        } else if(command.equals("stringify")) {
            return Helper.objectToJSONString(this);
        } else {
            System.out.println("Command " + command + " not found.");
            return null;
        }
    }
}
