package com.comp3004.educationmanager.strategy;

import com.comp3004.educationmanager.composite.Component;
import com.comp3004.educationmanager.composite.CourseContent;
import com.comp3004.educationmanager.decorator.EditableDecorator;
import com.comp3004.educationmanager.decorator.FileDecorator;
import com.comp3004.educationmanager.decorator.TextDecorator;

public class AddForumPostStrategy implements Strategy {
    @Override
    public Component createCourseItem(String name, String path) {
        Component content = new CourseContent(name, path);
        content = new FileDecorator(content);
        content = new TextDecorator(content);
        content = new EditableDecorator(content);
        return content;
    }
}
