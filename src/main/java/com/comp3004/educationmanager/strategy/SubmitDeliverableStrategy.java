package com.comp3004.educationmanager.strategy;

import com.comp3004.educationmanager.composite.Component;
import com.comp3004.educationmanager.composite.CourseItem;
import com.comp3004.educationmanager.decorator.EditableDecorator;
import com.comp3004.educationmanager.decorator.FileDecorator;
import com.comp3004.educationmanager.decorator.GradeableDecorator;

public class SubmitDeliverableStrategy implements Strategy {
    @Override
    public Component createCourseItem() {
        Component content = new CourseItem();
        content = new GradeableDecorator(content);
        content = new FileDecorator(content);
        content = new EditableDecorator(content);
        return content;
    }
}
