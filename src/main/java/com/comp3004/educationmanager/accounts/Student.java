package com.comp3004.educationmanager.accounts;



import com.comp3004.educationmanager.observer.CourseData;
import org.springframework.web.socket.TextMessage;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/*
Class for the Student user
Implements all student-specific functionality, including the observer
 */
@Entity
public class Student extends User {

    long studentID;


    @Transient
    ArrayList<String> pastCourses = new ArrayList<>();
    @Transient
    HashMap<String, CourseData> courses = new HashMap<>();

    @Override
    public void update(String command, Object value) {
        if (command.equals("deleteCourse")) {
            String courseCode = (String) value;
            courses.remove(courseCode);
            
            try {
                TextMessage change = new TextMessage("A course of yours has been deleted");
                this.socketConnection.sendMessage(change); //change should consist of courses
            } catch (IOException e) {
                e.printStackTrace(System.out);
            }
        } else if(command.equals("addCourse")) {
            addCourse((CourseData) value);
        }
        //session.sendMessage(); //get new courses
    }

    public void setStudentID(long studentID) {
        this.studentID = studentID;
    }

    public long getStudentID() {
        return this.studentID;
    }

    public void addCourse(CourseData data) {
        //TODO: Check to make sure student meets pre-req, course is not full and students timetable has no conflicts and it is not past deadline
        courses.put(data.getCourseCode(), data);
    }

    public void removeCourse(String courseCode) {
        //TODO: Check to see if student must be deleted if last course is removed
        courses.remove(courseCode);
    }

    public HashMap<String, CourseData> getCourses() {
        return courses;
    }
    public ArrayList<String> getPastCourses() {
        return pastCourses;
    }

    public void addPastCourse (String courseCode) {
        pastCourses.add(courseCode);
    }


    //Function to determine if a student meets the prerequisites to a course
    public boolean doesStudentMeetPrerequisites(ArrayList<String> prerequisites) {
        boolean hasPrerequisites = false;
        int prerequisiteCount = 0;
        if (prerequisites.size() > 0) {
            for (String prerequisite : prerequisites) {
                if (Arrays.stream(pastCourses.toArray()).anyMatch(prerequisite::equals)) {
                    System.out.println("Student does have course:   " + prerequisite);
                    prerequisiteCount++;
                }
                else {
                    System.out.println("Student does not have course:   " + prerequisite);
                }
            }

            if (prerequisiteCount == prerequisites.size()) {
                hasPrerequisites = true;
            }

            return hasPrerequisites;

        }
        else {
            return true;
        }


    }

}
