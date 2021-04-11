package com.comp3004.educationmanager.accounts;



import com.comp3004.educationmanager.observer.CourseData;
import jdk.internal.net.http.common.Pair;
import org.springframework.web.socket.TextMessage;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.IOException;
import java.util.*;

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
    @Transient
    HashMap<String, Integer> finalGrades = new HashMap<>();

    @Override
    public void update(String command, Object value) {
        switch (command) {
            case "deleteCourse":
                String courseCode = (String) value;
                courses.remove(courseCode);
                try {
                    TextMessage change = new TextMessage("get-courses");
                    if (this.getSocketConnection() == null || !this.getSocketConnection().isOpen()) {
                        System.out.println("Unable to connect to the student");
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
            case "finalGrade":
                Pair<String, Integer> pair = (Pair) value;
                finalGrades.put(pair.first, pair.second);
                break;
            case "get-courses": {
                TextMessage message = new TextMessage("get-courses");
                try {
                    if (this.getSocketConnection() != null && this.getSocketConnection().isOpen()) {
                        this.getSocketConnection().sendMessage(message);
                    } else {
                        System.out.println("Unable to connect to student");
                    }
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                }
                break;
            }
            case "get-global-courses": {
                TextMessage message = new TextMessage("get-global-courses");
                try {
                    if (this.getSocketConnection() != null && this.getSocketConnection().isOpen()) {
                        this.getSocketConnection().sendMessage(message);
                    } else {
                        System.out.println("Unable to connect to student");
                    }
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                }
                break;
            }
            case "removal-from-system": {
                TextMessage message = new TextMessage("removal-from-system");
                try {
                    if (this.getSocketConnection() != null && this.getSocketConnection().isOpen()) {
                        this.getSocketConnection().sendMessage(message);
                    } else {
                        System.out.println("Unable to connect to student");
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
                        System.out.println("Unable to connect to student");
                    }
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                }
                break;
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


    //Function to determine if a student has the prerequisites for a course
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



    //Function used to return status on course registration.
    //Returns different int depending on status of course registration:
    //0 = Success
    //1 = Course Registration Deadline has Passed
    //2 = Course is Full
    //3 = Student does not meet prerequisites
    //4 = Course does not fit student timetable
    //Rather than returning boolean, it is more useful to return status so
    //front-end can display to user reason for not being able to register
    public int canStudentRegisterInCourse(CourseData courseData, Calendar currDate, Calendar registrationDeadline) {

        ArrayList<String> days = courseData.getDays();

        HashMap<String, CourseData> studentCourses = courses;

        int timeConflicts = 0;

        //Loop to determine timetable conflicts
        for (String day : days) {
            for (CourseData studentCourseData : studentCourses.values()) {
                for (String day2: studentCourseData.getDays()) {
                    if (day.equals(day2)) {
                        //Course is on same day as another course, check if there is time conflict
                        String startTime = courseData.getStartTime();

                        String startTimeStudentCourse = studentCourseData.getStartTime();

                        String[] startTimeArr = startTime.split(":");

                        int startTimeMinutes = (Integer.parseInt(startTimeArr[0]) * 60 ) + Integer.parseInt(startTimeArr[1]);

                        String[] startTimeStudentCourseArr = startTimeStudentCourse.split(":");

                        int startTimeStudentCourseMinutes = (Integer.parseInt(startTimeStudentCourseArr[0]) * 60 ) + Integer.parseInt(startTimeStudentCourseArr[1]);

                        //Verifying if new course would be before or after current course.
                        //If the course is neither before or after then there must be a timetable conflict
                        if (!((startTimeMinutes + (courseData.getClassDuration() * 60) < startTimeStudentCourseMinutes) ||
                                (startTimeMinutes > startTimeStudentCourseMinutes + (studentCourseData.getClassDuration() * 60)))) {
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

        //Course Registration Deadline Passed
        if (currDate.compareTo(registrationDeadline) >= 0) {
            return 1;
        }
        //Course is Full
        else if (courseData.isCourseFull()) {
            return 2;
        }
        //Student does not meet prerequisites
        else if (!doesStudentMeetPrerequisites(courseData.getPrerequisites())) {
           return 3;
        }
        //Student has timetable conflicts
        else if (timeConflicts > 0) {
            return 4;
        }
        //Success: There are no conflicts.
        else {
            return 0;
        }

    }

    public boolean canStudentWithdraw(Calendar currDate, Calendar widthdrawDeadline) {
        return currDate.compareTo(widthdrawDeadline) < 0;
    }




}
