package com.comp3004.educationmanager.factory;

import com.comp3004.educationmanager.observer.CourseData;

public class CourseCreator {
    public CourseData createCourse(String courseCode, String courseName, int maxStudents) {
        CourseData courseData = new CourseData();
        courseData.setCourseCode(courseCode);
        courseData.setCourseName(courseName);
        courseData.setMaxStudents(maxStudents);

        return courseData;
    }
}
