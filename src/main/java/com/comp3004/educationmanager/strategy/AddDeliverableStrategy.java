package com.comp3004.educationmanager.strategy;

import com.comp3004.educationmanager.composite.Component;
import com.comp3004.educationmanager.composite.CourseContent;
import com.comp3004.educationmanager.decorator.DeadlineDecorator;
import com.comp3004.educationmanager.decorator.EditableDecorator;

public class AddDeliverableStrategy implements Strategy {
    @Override
    public Component createCourseItem(String name, String path, String type) {
        Component content = new CourseContent(name, path, type);
        content = new DeadlineDecorator(content);
        content = new EditableDecorator(content);
        return content;
    }

    @Override
    public Component createCourseItem(String name, String path, String type, boolean visible) {
        return null;
    }
}
