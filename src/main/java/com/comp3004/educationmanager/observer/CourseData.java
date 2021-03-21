package com.comp3004.educationmanager.observer;

public class CourseData extends Subject {
    public void updateAll() {
        //notify all observers (professors and students) who are attached to this course.
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
