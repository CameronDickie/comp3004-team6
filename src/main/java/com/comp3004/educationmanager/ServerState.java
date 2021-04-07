package com.comp3004.educationmanager;


import com.comp3004.educationmanager.accounts.Admin;
import com.comp3004.educationmanager.accounts.Professor;
import com.comp3004.educationmanager.accounts.Student;
import com.comp3004.educationmanager.accounts.User;
import com.comp3004.educationmanager.db.H2;
import com.comp3004.educationmanager.observer.CourseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class ServerState {
    @Autowired
    H2 db;

    HashMap<String, CourseData> courses = new HashMap<>();
    public static Admin admin;
    public static HashMap<Long, User> users = new HashMap<>();
    Calendar date = Calendar.getInstance();
    Calendar lastRegistrationDate = Calendar.getInstance();




    public ServerState () {
        System.out.println("Server state is being made...");

        int delay = 10000; //10000 milliseconds (10 seconds)

        //Setting date to January 1st 2021 at 0:00
        date.set(Calendar.MONTH, Calendar.JANUARY);
        date.set(Calendar.DAY_OF_MONTH, 1);
        date.set(Calendar.HOUR_OF_DAY , 0);
        date.set(Calendar.MINUTE ,0);
        date.set(Calendar.SECOND ,0);
        date.set(Calendar.MILLISECOND, 0);

        //Setting last registration date to random date of January 20th
        lastRegistrationDate.set(Calendar.MONTH, Calendar.JANUARY);
        lastRegistrationDate.set(Calendar.DAY_OF_MONTH, 20);
        lastRegistrationDate.set(Calendar.HOUR_OF_DAY , 0);
        lastRegistrationDate.set(Calendar.MINUTE ,0);
        lastRegistrationDate.set(Calendar.SECOND ,0);
        lastRegistrationDate.set(Calendar.MILLISECOND, 0);

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                date.add(Calendar.DATE,1);
                System.out.println(date.getTime());
            }
        };

        timer.schedule(timerTask, 0, 3000);
    }
    public boolean createUser(User u) {
        boolean res = db.addUser(u);
        if(res) {
            if (u instanceof Student) {
                users.put(((Student) u).getStudentID(), u);
            }
            else if (u instanceof Professor){
                users.put(((Professor) u).getProfessorID(), u);
            }
        }
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
