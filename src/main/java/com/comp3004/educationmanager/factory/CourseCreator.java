package com.comp3004.educationmanager.factory;

import com.comp3004.educationmanager.observer.CourseData;

public class CourseCreator {
    public CourseData createCourse(String courseCode, String courseName, int maxStudents) {
        return new CourseData(courseCode, courseName, maxStudents);
    }
}
