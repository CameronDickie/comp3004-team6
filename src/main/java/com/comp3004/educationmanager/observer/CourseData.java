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

    static int currStudents = 0;
    ArrayList<String> prerequisites = new ArrayList<>();

    String startTime;
    int classDuration;


    /*
    Constructor
     */
    public CourseData() {
        this.courseCode = "COUR1234A";
        this.courseName = "Course Placeholder";
        this.maxStudents = 0;
        this.content = new CourseContent(this.courseCode, "/", "section");
    }

    public CourseData(String courseCode, String courseName, int maxStudents) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.maxStudents = maxStudents;
        this.content = new CourseContent(this.courseCode, "/", "section");
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

}
