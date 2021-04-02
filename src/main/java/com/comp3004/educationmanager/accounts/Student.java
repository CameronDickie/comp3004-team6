package com.comp3004.educationmanager.accounts;

import com.comp3004.educationmanager.observer.Observer;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

/*
Class for the Student user
Implements all student-specific functionality, including the observer
 */
@Entity
public class Student extends User implements Observer{

    long studentID;

    List<String> courses = new ArrayList<>();
    @Override
    public void update(String command, Object value) {
        if (command.equals("deleteCourse")) {
            String courseCode = (String) value;
            courses.remove(courseCode);
        }
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
