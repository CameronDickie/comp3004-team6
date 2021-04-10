package com.comp3004.educationmanager.accounts;

import com.comp3004.educationmanager.observer.CourseData;
import org.springframework.web.socket.TextMessage;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
Class for the Professor user
Implements all professor-specific functionality, including the observer
 */
@Entity
public class Professor extends User {
    long professorID;

    @Transient
    HashMap<String, CourseData> courses = new HashMap<>();

    @Override
    public void update(String command, Object value) {
        if (command.equals("deleteCourse")) {
            String courseCode = (String) value;
            courses.remove(courseCode);
        } else if(command.equals("addCourse")) {
            addCourse((CourseData) value);
        } else if(command.equals("get-courses")) {
            TextMessage message = new TextMessage("get-courses");
            try {
                if(this.getSocketConnection() != null && this.getSocketConnection().isOpen()) {
                    this.getSocketConnection().sendMessage(message);
                } else {
                    System.out.println("Unable to connect to professor");
                }
            } catch (IOException e) {
                e.printStackTrace(System.out);
            }
        }
    }

    public void addCourse(CourseData data) {
        //TODO: Check to make sure student meets pre-req, course is not full and students timetable has no conflicts and it is not past deadline
        courses.put(data.getCourseCode(), data);
    }
    public void setProfessorID(long professorID) {
        this.professorID = professorID;
    }

    public long getProfessorID(){
        return this.professorID;
    }

    public HashMap<String, CourseData> getCourses() {
        return courses;
    }
}
