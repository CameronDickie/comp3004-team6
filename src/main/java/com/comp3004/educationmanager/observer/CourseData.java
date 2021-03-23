package com.comp3004.educationmanager.observer;

import com.comp3004.educationmanager.composite.Component;

public class CourseData extends Subject {
    int courseID;
    String courseName;
    Component content;

    public CourseData() {

    }

    public void updateAll() {
        //notify all observers (professors and students) who are attached to this course.
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
