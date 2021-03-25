package com.comp3004.educationmanager.strategy;

import com.comp3004.educationmanager.composite.Component;
import com.comp3004.educationmanager.composite.CourseItem;
import com.comp3004.educationmanager.decorator.EditableDecorator;
import com.comp3004.educationmanager.decorator.FileDecorator;

public class AddDocumentStrategy implements Strategy {
    @Override
    public Component createCourseItem() {
        Component content = new CourseItem();
        content = new FileDecorator(content);
        content = new EditableDecorator(content);
        return content;
    }
}
