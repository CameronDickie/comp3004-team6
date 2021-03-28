package com.comp3004.educationmanager.observer;

import com.comp3004.educationmanager.composite.Component;
import com.comp3004.educationmanager.composite.CourseContent;
import com.comp3004.educationmanager.composite.CourseItem;
import com.comp3004.educationmanager.strategy.Strategy;

import javax.persistence.*;

@Entity
public class CourseData extends Subject implements java.io.Serializable{

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
    Strategy createItemStrategy;

    public CourseData() {
        this.content = new CourseItem();
    }


    public CourseData(String courseCode, String courseName, int maxStudents) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.maxStudents = maxStudents;
        this.content = new CourseContent();
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setObject(byte[] object) { this.object = object; }

    public void setMaxStudents(int maxStudents) {
       this.maxStudents = maxStudents;
    }

    public int getMaxStudents() {
        return this.maxStudents;
    }

    public Component getContent() {
        return this.content;
    }

    public void addContent(String property, Object value) {
        content.setProperty(property, value);
    }

    public String getCourseCode() {
        return this.courseCode;
    }

    public byte[] getObject() { return object; }

    public void updateAll() {
        //notify all observers (professors and students) who are attached to this course.
        for (Observer observer : observers) {
            observer.update();
        }
    }

}
