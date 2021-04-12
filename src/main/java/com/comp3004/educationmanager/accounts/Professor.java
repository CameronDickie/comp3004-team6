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
        switch (command) {
            case "deleteCourse":
                String courseCode = (String) value;
                courses.remove(courseCode);
                try {
                    TextMessage change = new TextMessage("get-courses");
                    if (this.getSocketConnection() == null || !this.getSocketConnection().isOpen()) {
                        System.out.println("Unable to connect to the professor");
                        return;
                    }
                    this.socketConnection.sendMessage(change); //change should consist of courses
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                }
                break;
            case "addCourse":
                addCourse((CourseData) value);
                break;
            case "get-courses": {
                TextMessage message = new TextMessage("get-courses");
                try {
                    if (this.getSocketConnection() != null && this.getSocketConnection().isOpen()) {
                        this.getSocketConnection().sendMessage(message);
                    } else {
                        System.out.println("Unable to connect to professor");
                    }
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                }
                break;
            }
            case "get-global-courses": {
                TextMessage message = new TextMessage("get-global-courses");
                try {
                    if (this.getSocketConnection() != null) {
                        this.getSocketConnection().sendMessage(message);
                    } else {
                        System.out.println("Unable to connect to professor");
                    }
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                }
                break;
            }
            case "get-course-content": {
                TextMessage message = new TextMessage("get-course-content");
                try {
                    if (this.getSocketConnection() != null && this.getSocketConnection().isOpen()) {
                        this.getSocketConnection().sendMessage(message);
                    } else {
                        System.out.println("Unable to connect to professor");
                    }
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                }
                break;
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

    public boolean canProfessorBeAssignedToCourse(CourseData courseData) {

        ArrayList<String> days = courseData.getDays();

        HashMap<String, CourseData> professorCourses = courses;

        int timeConflicts = 0;

        //Loop to determine timetable conflicts
        for (String day : days) {
            for (CourseData professorCourseData : professorCourses.values()) {
                for (String day2: professorCourseData.getDays()) {
                    if (day.equals(day2)) {
                        //Course is on same day as another course, check if there is time conflict
                        String startTime = courseData.getStartTime();

                        String startTimeStudentCourse = professorCourseData.getStartTime();

                        String[] startTimeArr = startTime.split(":");

                        int startTimeMinutes = (Integer.parseInt(startTimeArr[0]) * 60 ) + Integer.parseInt(startTimeArr[1]);

                        String[] startTimeStudentCourseArr = startTimeStudentCourse.split(":");

                        int startTimeStudentCourseMinutes = (Integer.parseInt(startTimeStudentCourseArr[0]) * 60 ) + Integer.parseInt(startTimeStudentCourseArr[1]);

                        //Verifying if new course would be before or after current course.
                        //If the course is neither before or after then there must be a timetable conflict
                        if (!((startTimeMinutes + (courseData.getClassDuration() * 60) < startTimeStudentCourseMinutes) ||
                                (startTimeMinutes > startTimeStudentCourseMinutes + (professorCourseData.getClassDuration() * 60)))) {
                            timeConflicts++;
                            System.out.println("Conflict for: " + day2 + " " + " at " + startTimeStudentCourse);
                        }
                        else {
                            System.out.println("No conflict for: " + day2 + " " + " at " + startTimeStudentCourse);
                        }
                    }
                }
            }
        }

        return timeConflicts == 0;
    }
}
