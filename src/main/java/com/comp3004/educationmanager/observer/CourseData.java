package com.comp3004.educationmanager.observer;

import com.comp3004.educationmanager.accounts.Professor;
import com.comp3004.educationmanager.accounts.Student;
import com.comp3004.educationmanager.composite.Component;
import com.comp3004.educationmanager.composite.CourseContent;
import com.comp3004.educationmanager.composite.CourseItem;
import com.comp3004.educationmanager.strategy.Strategy;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CourseData extends Subject implements java.io.Serializable {
    protected static long id = 0;

    long localID;

    String courseCode;

    String courseName;

    int maxStudents;

    byte[] object;

    Component content;

    Strategy strategy;

    int currStudents = 0;
    ArrayList<String> prerequisites = new ArrayList<>();

    ArrayList<String> days = new ArrayList<>();
    String startTime;
    double classDuration;


    /*
    Constructor
     */
    public CourseData() {
        this.courseCode = "COUR1234A";
        this.courseName = "Course Placeholder";
        this.maxStudents = 0;
        this.content = new CourseContent(this.courseCode, "/", "section");
        this.localID = id;
        id++;
    }

    public CourseData(String courseCode, String courseName, int maxStudents, ArrayList<String>days, String startTime, double classDuration, ArrayList<String> prerequisites) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.maxStudents = maxStudents;
        this.content = new CourseContent(this.courseCode, "/", "section");

        this.days = days;
        this.startTime = startTime;
        this.classDuration = classDuration;
        this.prerequisites = prerequisites;
        this.localID = id;
        id++;
    }

    /*
    Subject attach method
     */
    @Override
    public boolean attach(Observer o) {
        o.update("addCourse", this);

        if (o instanceof Student) {
            currStudents++;

        }
        return observers.add(o);
    }

    @Override
    public boolean detach(Observer o) {
        currStudents--;
        return observers.remove(o);
    }

    /*
    Subject Notify All Method
     */
    public void updateAll(String command, Object value) {
        //notify all observers (professors and students) who are attached to this course.
        for (Observer observer : observers) {
            observer.update(command, value);
        }
    }

    /*
    Method to add content to the course
    TODO:
        - Should new component be returned or the entire content structure?
            * currently returning new component, will have a separate method to return whole structure
     */
    public Component addContent(String name, String path, String type) {
        Component comp = strategy.createCourseItem(name, path, type);
        content.executeCommand("addItem", comp);
        return comp;
    }

    public Component addContent(String name, String path, String type, boolean visible) {
        Component comp = strategy.createCourseItem(name, path, type, visible);
        content.executeCommand("addItem", comp);
        return comp;
    }

    /*
    Getters
     */

    public Long getCourseID() { return this.localID; }

    public String getCourseCode() {
        return this.courseCode;
    }

    public int getMaxStudents() {
        return this.maxStudents;
    }

    public Component getContent() {
        return this.content;
    }

    public String getCourseName() { return this.courseName; }

    public byte[] getObject() { return object; }

    public Professor getProfessor() {
        if(!(this.observers.get(0) instanceof Professor)) {
            System.out.println("Could not find the professor for this course");
            return null;
        }
        return (Professor) this.observers.get(0);
    }

    /*
    Setters
     */

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setObject(byte[] object) { this.object = object; }

    public void printProfessor() {
        System.out.println(observers.size());
        Professor prof = (Professor) observers.get(0);
        System.out.println(prof.getName());
    }

    public void setMaxStudents(int maxStudents) {
       this.maxStudents = maxStudents;
    }

    public void setStrategy(Strategy strategy) { this.strategy = strategy; }

    public int getCurrStudents() {
        return this.currStudents;
    }

    public boolean isCourseFull (){
        return this.currStudents == this.maxStudents;
    }

    public void addPrerequisite(String courseCode) {
        this.prerequisites.add(courseCode);
    }

    public ArrayList<String> getPrerequisites() {
        return this.prerequisites;
    }

    public ArrayList<String> getDays() {
        return this.days;
    }

    public String getStartTime() {return this.startTime;}

    public double getClassDuration() {
        return this.classDuration;
    }
}
