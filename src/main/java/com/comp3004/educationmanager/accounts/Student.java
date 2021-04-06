package com.comp3004.educationmanager.accounts;



import org.springframework.web.socket.TextMessage;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
Class for the Student user
Implements all student-specific functionality, including the observer
 */
@Entity
public class Student extends User {

    long studentID;


    @Transient
    List<String> courses = new ArrayList<>();

    @Override
    public void update(String command, Object value) {
        if (command.equals("deleteCourse")) {
            String courseCode = (String) value;
            courses.remove(courseCode);
            try {
                TextMessage change = new TextMessage("A course of yours has been deleted");
                this.socketConnection.sendMessage(change);
            } catch (IOException e) {
                e.printStackTrace(System.out);
            }

        }
        //session.sendMessage(); //get new courses
    }

    public void setStudentID(long studentID) {
        this.studentID = studentID;
    }

    public long getStudentID() {
        return this.studentID;
    }

    public void addCourse(String courseCode) {
        //TODO: Check to make sure student meets pre-req, course is not full and students timetable has no conflicts and it is not past deadline
        courses.add(courseCode);
    }

    public void removeCourse(String courseCode) {
        //TODO: Check to see if student must be deleted if last course is removed
        courses.remove(courseCode);
    }

}
