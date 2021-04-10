package com.comp3004.educationmanager.strategy;

import com.comp3004.educationmanager.composite.Component;
import com.comp3004.educationmanager.composite.CourseItem;
import com.comp3004.educationmanager.decorator.EditableDecorator;
import com.comp3004.educationmanager.decorator.FileDecorator;

public class AddDocumentStrategy implements Strategy {
    @Override
    public Component createCourseItem(String name, String path, String type, long userID, String userType, boolean visible) {
        Component content = new CourseItem(name, path, type, userID, userType, visible);
        content = new FileDecorator(content);
        content = new EditableDecorator(content);
        return content;
    }
}
