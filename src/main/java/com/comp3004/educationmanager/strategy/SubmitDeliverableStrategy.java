package com.comp3004.educationmanager.strategy;

import com.comp3004.educationmanager.composite.Component;
import com.comp3004.educationmanager.composite.CourseContent;
import com.comp3004.educationmanager.decorator.EditableDecorator;
import com.comp3004.educationmanager.decorator.GradeableDecorator;

public class SubmitDeliverableStrategy implements Strategy {
    @Override
    public Component createCourseItem(String name, String path, String type, long userID, String userType, boolean visible) {
        Component content = new CourseContent(name, path, type, userID, userType, visible);
        content = new GradeableDecorator(content);
        content = new EditableDecorator(content);
        return content;
    }
}
