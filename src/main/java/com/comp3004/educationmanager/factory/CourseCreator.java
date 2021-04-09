package com.comp3004.educationmanager.factory;

import com.comp3004.educationmanager.observer.CourseData;

import java.util.ArrayList;

public class CourseCreator {
    public CourseData createCourse(String courseCode, String courseName, int maxStudents, ArrayList<String> days, String startTime, double classDuration, ArrayList<String> prerequisites) {
        return new CourseData(courseCode, courseName, maxStudents, days, startTime, classDuration, prerequisites);
    }
}
