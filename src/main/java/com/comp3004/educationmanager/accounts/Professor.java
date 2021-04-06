package com.comp3004.educationmanager.accounts;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/*
Class for the Professor user
Implements all professor-specific functionality, including the observer
 */
@Entity
public class Professor extends User {
    long professorID;

    @Transient
    ArrayList<String> courses = new ArrayList<>();

    @Override
    public void update(String command, Object value) {
        if (command.equals("deleteCourse")) {
            String courseCode = (String) value;
            courses.remove(courseCode);
        } else if(command.equals("addCourse")) {
            courses.add((String) value);
        }
    }

    public void setProfessorID(long professorID) {
        this.professorID = professorID;
    }

    public long getProfessorID(){
        return this.professorID;
    }

    public ArrayList<String> getCourses() {
        return courses;
    }
}
