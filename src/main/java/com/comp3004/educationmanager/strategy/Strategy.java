package com.comp3004.educationmanager.strategy;

import com.comp3004.educationmanager.composite.Component;

public interface Strategy {
    Component createCourseItem(String name, String path, String type);
    Component createCourseItem(String name, String path, String type, boolean visible);
}
