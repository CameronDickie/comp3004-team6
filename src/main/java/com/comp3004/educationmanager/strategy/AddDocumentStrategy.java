package com.comp3004.educationmanager.strategy;

import com.comp3004.educationmanager.composite.Component;
import com.comp3004.educationmanager.composite.CourseItem;
import com.comp3004.educationmanager.decorator.EditableDecorator;
import com.comp3004.educationmanager.decorator.FileDecorator;

public class AddDocumentStrategy implements Strategy {
    @Override
    public Component createCourseItem(String name, String path) {
        Component content = new CourseItem(name, path);
        content = new FileDecorator(content);
        content = new EditableDecorator(content);
        return content;
    }
}
