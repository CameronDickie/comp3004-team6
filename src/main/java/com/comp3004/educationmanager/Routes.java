package com.comp3004.educationmanager;
import com.comp3004.educationmanager.accounts.User;
import com.comp3004.educationmanager.composite.Component;
import com.comp3004.educationmanager.composite.CourseContent;
import com.comp3004.educationmanager.decorator.EditableDecorator;
import com.comp3004.educationmanager.factory.CourseCreator;
import com.comp3004.educationmanager.factory.StudentCreator;
import com.comp3004.educationmanager.observer.CourseData;
import com.comp3004.educationmanager.observer.CourseDataSerialized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.MediaType;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import com.comp3004.educationmanager.misc.SerializationHelper;

@RestController
public class Routes {
    // For things like converting Objects to json
    Helper help = new Helper();
    @Autowired
    ServerState s;
    SerializationHelper serializationHelper = new SerializationHelper();


    @GetMapping("/api/members")
    public String members() {
        return "Cameron, Cameron, Ben, Jaxson";
    }

    @PostMapping(value ="/api/register", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String register(@RequestBody String info) {
        System.out.println("From '/api/register': " + info);
        HashMap<String, String> map = help.stringToMap(info);
        //this is the notification to be added to the admin's list of notifications -- likely to be a part of the database, but for now I just want to get it all working
        User newUser = new StudentCreator().createUser(map.get("firstname") + map.get("lastname"), map.get("password"));
        s.createUser(newUser);
        s.print();
        return info + " has attempted to be registered";
    }

    @PostMapping(value = "/api/login", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String login(@RequestBody String userinfo) {
        System.out.println("From '/api/login': " + userinfo);
        HashMap<String, String> map = help.stringToMap(userinfo);
        //using this userinfo, see if there is a user with this information (auth)
        String answer = "";
        System.out.println(map.get("username"));
        System.out.println(map.get("password"));
        try {
            if (s.auth(map.get("username"), map.get("password"))) {
                answer = "true";
            } else {
                answer = "false";
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        s.print();
        return answer;
    }

    @PostMapping(value ="/api/create-course", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String createCourse(@RequestBody String courseInfo) throws IOException, ClassNotFoundException {
        System.out.println("From '/api/create-course': " + courseInfo);

        HashMap <String, String> courseMap = help.stringToMap(courseInfo);

        CourseData courseData = new CourseCreator().createCourse(courseMap.get("courseCode"), courseMap.get("courseName"), Integer.parseInt(courseMap.get("maxStudents")));

        courseData.addContent("path", "123/123/123");

        CourseDataSerialized courseDataSerialized = (CourseDataSerialized) serializationHelper.createSerializedObject(courseData, "course");

        s.createCourse(courseData);

        s.createCourseSerialized(courseDataSerialized);

        return courseInfo + " has been created";
    }

    @PostMapping(value ="/api/delete-course", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String deleteCourse(@RequestBody String courseInfo) throws IOException, ClassNotFoundException {
        System.out.println("From '/api/delete-course': " + courseInfo);

        //Needs to delete courses AND delete students / professors with course

        CourseDataSerialized courseDataSerialized = s.getCourseSerialized(5);

        Object courseDataObject = serializationHelper.deserializeObject(courseDataSerialized.getObj(), "course");

        CourseData courseData = (CourseData) courseDataObject;

        Component comp = courseData.getContent();
        System.out.println("COMPONNENT (COURSE DELETION): " + comp.getProperty("path"));

        System.out.println("MAXIMUM STUDENTS (COURSE DELETION):   "  + courseData.getMaxStudents());

        System.out.println("COURSE CODE (COURSE DELETION):   "  + courseData.getCourseCode());

        HashMap <String, String> courseMap = help.stringToMap(courseInfo);

        String courseCode = courseMap.get("courseCode");

        s.deleteCourse(courseCode);

        return courseInfo + " has been deleted";
    }

    @PostMapping(value ="/api/course-registration", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String courseRegistration(@RequestBody String studentInfo) {
        System.out.println("From '/api/delete-course': " + studentInfo);

        //Needs to delete courses AND delete students / professors with course

        HashMap <String, String> studentMap = help.stringToMap(studentInfo);

        return studentInfo + " has been deleted";
    }



}



