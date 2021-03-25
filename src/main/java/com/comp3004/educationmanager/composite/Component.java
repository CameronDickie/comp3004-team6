package com.comp3004.educationmanager.composite;

public interface Component {
    boolean setProperty(String property, Object value);
    Object getProperty(String property);
    void executeCommand(String command, Object value);
}
