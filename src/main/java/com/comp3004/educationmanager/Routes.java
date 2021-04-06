package com.comp3004.educationmanager;
import com.comp3004.educationmanager.accounts.Professor;
import com.comp3004.educationmanager.accounts.Student;
import com.comp3004.educationmanager.accounts.User;
import com.comp3004.educationmanager.composite.Component;
import com.comp3004.educationmanager.factory.CourseCreator;
import com.comp3004.educationmanager.factory.ProfessorCreator;
import com.comp3004.educationmanager.factory.StudentCreator;
import com.comp3004.educationmanager.misc.Serialization;
import com.comp3004.educationmanager.observer.CourseData;
import com.comp3004.educationmanager.strategy.AddDocumentStrategy;
import com.comp3004.educationmanager.strategy.CourseContentStrategy;
import com.comp3004.educationmanager.strategy.SubmitDeliverableStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.MediaType;

import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
public class Routes {
    // For things like converting Objects to json
//    Helper help = new Helper();
    @Autowired
    ServerState s;
    Serialization serialization = new Serialization();
    StudentCreator studentCreator = new StudentCreator();
    ProfessorCreator professorCreator = new ProfessorCreator();


    @GetMapping("/api/members")
    public String members() {
        return "Cameron, Cameron, Ben, Jaxson";
    }

    @PostMapping(value ="/api/register", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String register(@RequestBody String info) {
        System.out.println("From '/api/register': " + info);
        HashMap<String, String> map = Helper.stringToMap(info);
        //this is the notification to be added to the admin's list of notifications -- likely to be a part of the database, but for now I just want to get it all working
        User newUser = studentCreator.createUser(map.get("firstname") + map.get("lastname"), map.get("password"));

//        newUser.setSocketConnection(new MyTextWebSocketHandler());
//        s.addSocketConnection(newUser.getSocketConnection());
        s.createUser(newUser);
        s.print();
        return info + " has attempted to be registered";
    }

    @PostMapping(value ="/api/register-professor", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String registerProfessor(@RequestBody String info) {
        System.out.println("From '/api/register': " + info);
        HashMap<String, String> map = Helper.stringToMap(info);
        //this is the notification to be added to the admin's list of notifications -- likely to be a part of the database, but for now I just want to get it all working
        User newUser = professorCreator.createUser(map.get("firstname") + map.get("lastname"), map.get("password"));

        s.createUser(newUser);
        s.print();
        return info + " has attempted to be registered";
    }

    @PostMapping(value = "/api/login", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String login(@RequestBody String userinfo) {
        System.out.println("From '/api/login': " + userinfo);
        HashMap<String, String> map = Helper.stringToMap(userinfo);
        //using this userinfo, see if there is a user with this information (auth)
        String answer = "";
        System.out.println(map.get("username"));
        System.out.println(map.get("password"));
        try {
            if (s.auth(map.get("username"), map.get("password"))) {
                User ur = s.getUser(map.get("username"));
                String rs = Helper.objectToJSONString(ur);
                HashMap<String, String> mm = Helper.stringToMap(rs);

                mm.put("type", ur.getClass().toString());
                answer = Helper.objectToJSONString(mm);
            } else {
                answer = Helper.objectToJSONString(Helper.stringToMap("{error: true}"));
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

        //Creating HashMap of data sent in request

        HashMap <String, String> courseMap = Helper.stringToMap(courseInfo);

        CourseData courseData = new CourseCreator().createCourse(courseMap.get("courseCode"), courseMap.get("courseName"), Integer.parseInt(courseMap.get("maxStudents")));

        String courseCode = courseMap.get("courseCode");

        User user = s.users.get(courseMap.get("professorID")); //Retrieving User (The Professor) from List of Users

        Professor professor = (Professor) user; //Casting Professor to User
        courseData.attach(professor); //Attaching Professor to CourseData

        s.courses.put(courseMap.get("courseCode"), courseData); //Storing CourseData in courses hashmap

        return courseInfo + " has been created";
    }

    @PostMapping(value ="/api/delete-course", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String deleteCourse(@RequestBody String courseInfo) throws IOException, ClassNotFoundException {
        System.out.println("From '/api/delete-course': " + courseInfo);

        //Needs to delete courses AND delete students / professors with course

        HashMap <String, String> courseMap = Helper.stringToMap(courseInfo);  //Creating HashMap of data sent in request

        String courseCode = courseMap.get("courseCode");

        //Calling updateAll with command deleteCourse on all observers for courseData
        //This will remove the course from the course list stored within the class
        s.courses.get(courseCode).updateAll("deleteCourse", courseCode);

        //Removing course from list of courses
        s.courses.remove(courseCode);

        return courseInfo + " has been deleted";
    }

    @PostMapping(value ="/api/course-registration", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String courseRegistration(@RequestBody String studentInfo) {
        System.out.println("From '/api/course-registration': " + studentInfo);

        //Needs to delete courses AND delete students / professors with course

        HashMap <String, String> infoMap = Helper.stringToMap(studentInfo);   //Creating HashMap of data sent in request

        CourseData courseData = s.courses.get(infoMap.get("courseCode")); //Retrieving Course from list of courses

        User user = s.users.get(infoMap.get("studentNumber")); //Retrieving User (The Student Registering) From List of Users

        Student student = (Student) user; //Casting the User object to student
        courseData.attach(student);//Attaching Student to CourseData

        return studentInfo + " has been deleted";
    }

    @PostMapping(value ="/api/course-withdrawal", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String courseWithdrawal(@RequestBody String studentInfo) {
        System.out.println("From '/api/course-withdrawl': " + studentInfo);

        HashMap <String, String> infoMap = Helper.stringToMap(studentInfo);   //Creating HashMap of data sent in request

        CourseData courseData = s.courses.get(infoMap.get("courseCode")); //Retrieving Course from list of courses

        User user = s.users.get(infoMap.get("studentNumber")); //Retrieving User (The Student Registering) From List of Users

        Student student = (Student) user; //Casting the User object to student

        student.removeCourse(infoMap.get("courseCode")); //Removing course in list of courses in the student

        courseData.detach(student);//Detach student from course

        return studentInfo + " has been deleted";
    }

    /*
    Route for getting a student's courses
    USAGE:
    @param (userInfo JSON)
        - userId: ID of user to retrieve courses for
    @return an array of course content strings
    TODO:
        - how to query for user? right now I'm using the "userId" tag?
    */
    @GetMapping(value = "/api/get-user-courses", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String[] getUserCourses(@RequestBody String userInfo) {
        System.out.println("From '/api/get-user-courses: " + userInfo);

        HashMap<String, String> userMap = Helper.stringToMap(userInfo);
        HashMap<String, CourseData> courseMap = new HashMap<>();

        User user = s.users.get(userMap.get("userId"));
        if(user instanceof Student) {
            Student s = (Student) user;
            courseMap = s.getCourses();
        } else if(user instanceof Professor) {
            Professor p = (Professor) user;
            courseMap = p.getCourses();
        }

        String[] contentStrings = new String[courseMap.size()];
        int i = 0;
        for(CourseData course : courseMap.values()) {
            contentStrings[i++] = (String) course.getContent().executeCommand("stringify", null);
        }

        return contentStrings;
    }

    /*
    Route for adding a course content object
    USAGE:  Call this when the professor wants to create content items such as course sections, lectures,
            or deliverables. MUST be called before adding any related items like documents.
        - use a separate post with this component's path to add for adding additional items
    @param (contentInfo JSON)
        - courseCode: the course to add the content to
        - name: the name for the CourseContent object
        - path: the path for the CourseContent object
    @return the path of the item added
    TODO:
        - How do we want to decide the path for each item?
            - the addContent method returns a component (either entire course structure or specifically the new item?)
            - I could implement a method that "stringifies" a component - will just be a long string of all path names
                and the stucture of how they appear in the course, thereby providing an easy way to pass the whole
                structure to the front end and get the paths whenever a professor is editing the course
    */
    @PostMapping(value = "/api/add-content", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String addContent(@RequestBody String contentInfo) {
        HashMap<String, String> contentMap = Helper.stringToMap(contentInfo);
        CourseData course = s.getCourseData(contentMap.get("courseCode"));
        course.setStrategy(new CourseContentStrategy());
        Component comp = course.addContent(contentMap.get("name"), contentMap.get("path"), contentMap.get("type"));

        return (String) comp.getProperty("fullPath");
    }

    /*
    Route for submitting a course deliverable
    USAGE: Call this when the student wants to submit a course deliverable. MUST be called before adding any related documents.
        - use a separate post request for creating/attaching documents
    @params
        -
    TODO:
        - The path for the student's deliverable should probably be something along the lines of /COMP3004B/BENWILLIAMS/ASSIGNMENTPATH/
            - could we make this only visible to the one student?
     */
    @PostMapping(value = "/api/submit-deliverable", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String submitDeliverable(@RequestBody String contentInfo) {
        HashMap<String, String> contentMap = Helper.stringToMap(contentInfo);
        CourseData course = s.getCourseData(contentMap.get("courseCode"));
        course.setStrategy(new SubmitDeliverableStrategy());
        Component comp = course.addContent(contentMap.get("name"), contentMap.get("path"), contentMap.get("type"), false);
        comp.setProperty("type", contentMap.get("type"));

        return (String) comp.getProperty("fullPath");
    }

    /*
    Route for adding a document
    USAGE:  Must convert the file to a byte array and then encode that as a string (with Base64 encoding)
            to be passed in with the contentInfo param
        - this will decode the String into a byte array and pass that to the file decorator
    TODO:
        -
     */
    @PostMapping(value = "/api/add-document", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String addDocument(@RequestBody String contentInfo) {
        HashMap<String, String> contentMap = Helper.stringToMap(contentInfo);
        CourseData course = s.getCourseData(contentMap.get("courseCode"));

        String sBytes = contentMap.get("bytes");
        byte[] bytes = Base64.getDecoder().decode(sBytes);

        course.setStrategy(new AddDocumentStrategy());
        Component comp = course.addContent(contentMap.get("name"), contentMap.get("path"), contentMap.get("type"));
        comp.setProperty("file", bytes);

        return contentInfo + " has been submitted";
    }

    /*
    Route for adding a forum post
    TODO:
        -
     */
    @PostMapping(value = "/api/forum-post", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String addForumPost(@RequestBody String contentInfo) {
        HashMap<String, String> contentMap = Helper.stringToMap(contentInfo);


        return contentInfo + " has been submitted";
    }

    /*
    Route for submitting a deliverable grade(s) for a student(s)
    TODO:
        -
    */
    @PostMapping(value = "/api/add-grade", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String addGrade(@RequestBody String contentInfo) {
        HashMap<String, String> contentMap = Helper.stringToMap(contentInfo);
        CourseData course = s.getCourseData(contentMap.get("courseCode"));
        Component c = (Component) course.getContent().executeCommand("findByPath", contentMap.get("path"));
        c.executeCommand("addGrade", Integer.parseInt(contentMap.get("grade")));

        return contentInfo + " has been submitted";
    }

    /*
    Route for submitting the final grade for a student(s)
    TODO:
        - Are we using the visitor pattern for this?
        - How do we want to store the final grades, with the student?
     */
    @PostMapping(value = "/api/submit-final-grade", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String addFinalGrade(@RequestBody String contentInfo) {
        HashMap<String, String> contentMap = Helper.stringToMap(contentInfo);

        return contentInfo + " has been submitted";
    }

    /*
    Route for downloading a file
    TODO:
        -
     */
    @GetMapping(value = "/api/download-file", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] downloadFile(@RequestBody String contentInfo) {
        HashMap<String, String> contentMap = Helper.stringToMap(contentInfo);
        CourseData course = s.getCourseData(contentMap.get("courseCode"));
        Component c = (Component) course.getContent().executeCommand("findByPath", contentMap.get("path"));
        return (byte[]) c.executeCommand("download", null);
    }

    /*
    Route for view a file as PDF
    TODO:
        - Must still implement conversion feature within the FileDecorator class
     */
    @GetMapping(value = "/api/view-file", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] viewFile(@RequestBody String contentInfo) {
        HashMap<String, String> contentMap = Helper.stringToMap(contentInfo);
        CourseData course = s.getCourseData(contentMap.get("courseCode"));
        Component c = (Component) course.getContent().executeCommand("findByPath", contentMap.get("path"));
        return (byte[]) c.executeCommand("viewAsPDF", null);
    }
}



