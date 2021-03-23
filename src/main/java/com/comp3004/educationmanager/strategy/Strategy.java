package com.comp3004.educationmanager.strategy;

import com.comp3004.educationmanager.composite.Component;

public interface Strategy {
    /*
    CREATING NEW STRATEGIES:
    - Step 1: Create a new class for the strategy and implement the createCourseItem method
    - Step 2: Within the method, create a new Component object initialized as either a CourseItem or CourseContent object
    - Step 3: Decorate said component with the necessary concrete Decorators - MUST be in the following order:
        - 1: GradeableDecorator
        - 2: FileDecorator
        - 3: TextDecorator
        - 4: EditableDecorator
    - Step 4: return the component;
     */
    Component createCourseItem();
}
