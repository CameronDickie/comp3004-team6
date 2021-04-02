package com.comp3004.educationmanager.accounts;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

/*
Class for the Professor user
Implements all professor-specific functionality, including the observer
 */
@Entity
public class Professor extends User {
    long professorID;
    List<String> courses = new ArrayList<>();

    @Override
    public void update(String command, Object value) {
        if (command.equals("deleteCourse")) {
            String courseCode = (String) value;
            courses.remove(courseCode);
        }
    }

    public void setProfessorID(long professorID) {
        this.professorID = professorID;
    }

    public long getProfessorID(){
        return this.professorID;
    }

    public void addCourse(String courseCode) {
        courses.add(courseCode);
    }
}
