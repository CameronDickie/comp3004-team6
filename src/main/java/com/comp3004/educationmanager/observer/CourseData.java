package com.comp3004.educationmanager.observer;

import com.comp3004.educationmanager.accounts.Professor;
import com.comp3004.educationmanager.composite.Component;
import com.comp3004.educationmanager.composite.CourseContent;
import com.comp3004.educationmanager.composite.CourseItem;
import com.comp3004.educationmanager.strategy.Strategy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CourseData extends Subject implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @Column(name = "courseCode")
    String courseCode;

    @Column(name = "courseName")
    String courseName;

    @Column(name = "maxStudents")
    int maxStudents;

    @Column(name = "object")
    @Lob
    byte[] object;

    @Transient
    Component content;

    @Transient
    Strategy strategy;

    /*
    Constructor
     */
    public CourseData() {
        this.courseCode = "COUR1234A";
        this.courseName = "Course Placeholder";
        this.maxStudents = 0;
        this.content = new CourseContent(this.courseCode, "/");
    }

    public CourseData(String courseCode, String courseName, int maxStudents) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.maxStudents = maxStudents;
        this.content = new CourseContent(this.courseCode, "/");
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
            * currently returning entire structure
            * can also return a "stringified" version of the structure so we can pass it to the front end
                or have a separate GetMapping to retrieve the entire course structure
     */
    public Component addContent(String name, String path) {
        Component comp = strategy.createCourseItem(name, path);
        content.executeCommand("addItem", comp);
        return comp;
    }

    /*
    Getters
     */

    public String getCourseCode() {
        return this.courseCode;
    }

    public int getMaxStudents() {
        return this.maxStudents;
    }

    public Component getContent() {
        return this.content;
    }

    public byte[] getObject() { return object; }

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
}
