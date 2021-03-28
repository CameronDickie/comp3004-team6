package com.comp3004.educationmanager.composite;

/*
Base Component interface in the SDC Pattern
Implemented by the CourseContent, CourseItem, and all Decorators
 */
public interface Component {
    boolean setProperty(String property, Object value);
    Object getProperty(String property);
    void executeCommand(String command, Object value);
}
