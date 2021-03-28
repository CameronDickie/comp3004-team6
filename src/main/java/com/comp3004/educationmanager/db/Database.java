package com.comp3004.educationmanager.db;

import com.comp3004.educationmanager.accounts.User;
import com.comp3004.educationmanager.observer.CourseData;
import com.comp3004.educationmanager.observer.CourseDataSerialized;

public interface Database {
    void initialize();
    boolean addUser(User user);
    boolean addCourseData(CourseData courseData);
    boolean addSerializedCourseData(CourseDataSerialized courseDataSerialized);


    boolean deleteCourseData(String courseCode);
    boolean auth(String username, String password);
    void print();

}
