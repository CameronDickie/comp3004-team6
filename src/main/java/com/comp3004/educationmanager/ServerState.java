package com.comp3004.educationmanager;


import com.comp3004.educationmanager.accounts.User;
import com.comp3004.educationmanager.db.H2;
import com.comp3004.educationmanager.observer.CourseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;

@Component
public class ServerState {
    @Autowired
    H2 db;

    HashMap<String, CourseData> courses = new HashMap<>();
    public static HashMap<Long, User> users = new HashMap<>();

    public ServerState () {
        System.out.println("Server state is being made...");
    }
    public boolean createUser(User u) {
        boolean res = db.addUser(u);
        if(res) users.put(u.getUserId(), u);
        return res;
    }
    public void print() {
        db.print();
    }
    public boolean auth(String uname, String pword) {
        return db.auth(uname, pword);
    }
    public User getUser(String username) {
        return db.getUser(username);
    }
    public void createCourse(CourseData courseData) { courses.put(courseData.getCourseCode(), courseData); }
    public CourseData getCourseData(String courseCode) { return courses.get(courseCode); }

    public boolean deleteCourse(String courseCode) {return db.deleteCourseData(courseCode);}
}
