package com.comp3004.educationmanager;


import com.comp3004.educationmanager.accounts.Admin;
import com.comp3004.educationmanager.accounts.Professor;
import com.comp3004.educationmanager.accounts.Student;
import com.comp3004.educationmanager.accounts.User;
import com.comp3004.educationmanager.db.H2;
import com.comp3004.educationmanager.factory.ProfessorCreator;
import com.comp3004.educationmanager.factory.StudentCreator;
import com.comp3004.educationmanager.misc.Application;
import com.comp3004.educationmanager.observer.CourseData;
import com.comp3004.educationmanager.observer.SystemData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;


@Component
public class ServerState {
    @Autowired
    H2 db;
    //all three of these need to be moved to SystemData
    HashMap<String, CourseData> courses = new HashMap<>();
//    public static Admin admin;
//    public static HashMap<Long, User> users = new HashMap<>();

    boolean freezeTime = true;
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
                if(!freezeTime) {
                    date.add(Calendar.DATE, 1);
                    System.out.println(date.getTime());
                }
            }
        };

        timer.schedule(timerTask, 0, 3000);
    }
    public boolean createUser(User u) {
        boolean res = db.addUser(u);
        if(res) {
            if (u instanceof Student) {
                SystemData.users.put(((Student) u).getStudentID(), u);
            }
            else if (u instanceof Professor){
                SystemData.users.put(((Professor) u).getProfessorID(), u);
            } else if(u instanceof Admin) {
                SystemData.admin = (Admin) u;
            }
        }
        return res;
    }
    public boolean createUserFromApplication(Application a) {
        User newUser;
        if(a.getType().equals("student")) {
            newUser = new StudentCreator().createUser(a.getFirstname().toLowerCase() + a.getLastname().toLowerCase(), a.getPassword());
        } else if(a.getType().equals("professor")) {
            newUser = new ProfessorCreator().createUser(a.getFirstname().toLowerCase() + a.getLastname().toLowerCase(), a.getPassword());
        } else {
            newUser = null;
            System.out.println("Failed to make user from application form");
            return false;
        }
        return createUser(newUser);
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
    public void createCourse(CourseData courseData) { SystemData.courses.put(courseData.getCourseCode(), courseData); }
    public CourseData getCourseData(String courseCode) { return SystemData.courses.get(courseCode); }

    public boolean deleteCourse(String courseCode) {return db.deleteCourseData(courseCode);}



}
