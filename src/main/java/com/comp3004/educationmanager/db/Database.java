package com.comp3004.educationmanager.db;

import com.comp3004.educationmanager.accounts.User;
import com.comp3004.educationmanager.observer.CourseData;

public interface Database {
    void initialize();

    boolean addUser(User user);
    boolean auth(String username, String password);
    void print();

    CourseData getCourseData(String courseCode);
    boolean deleteCourseData(String courseCode);
    boolean addCourseData(CourseData courseData);
}
