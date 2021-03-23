package com.comp3004.educationmanager.strategy;

import com.comp3004.educationmanager.composite.Component;
import com.comp3004.educationmanager.composite.CourseContent;
import com.comp3004.educationmanager.decorator.EditableDecorator;

public class AddLectureStrategy implements Strategy {
    @Override
    public Component createCourseItem() {
        Component content = new CourseContent();
        content = new EditableDecorator(content);
        return content;
    }
}
