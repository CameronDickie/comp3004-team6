package com.comp3004.educationmanager;


import com.comp3004.educationmanager.accounts.User;
import com.comp3004.educationmanager.db.H2;
import com.comp3004.educationmanager.observer.CourseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;

@Component
public class ServerState {
    @Autowired
    H2 db;

    HashMap<String, CourseData> courses = new HashMap<String, CourseData>();
    HashMap<Long, User> users = new HashMap<Long, User>();



    public ServerState () {
        System.out.println("Server state is being made...");
    }
    public boolean createUser(User u) {
        return db.addUser(u);
    }
    public void print() {
        db.print();
    }
    public boolean auth(String uname, String pword) {
        return db.auth(uname, pword);
    }
    public boolean createCourse(CourseData courseData) {return db.addCourseData(courseData);}
    public CourseData getCourseData(String courseCode) { return db.getCourseData(courseCode); }

    public boolean deleteCourse(String courseCode) {return db.deleteCourseData(courseCode);}
}
