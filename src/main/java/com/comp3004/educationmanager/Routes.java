package com.comp3004.educationmanager;
import com.comp3004.educationmanager.accounts.Admin;
import com.comp3004.educationmanager.accounts.Professor;
import com.comp3004.educationmanager.accounts.Student;
import com.comp3004.educationmanager.accounts.User;
import com.comp3004.educationmanager.composite.Component;
import com.comp3004.educationmanager.factory.AdminCreator;
import com.comp3004.educationmanager.factory.CourseCreator;
import com.comp3004.educationmanager.factory.ProfessorCreator;
import com.comp3004.educationmanager.factory.StudentCreator;

import com.comp3004.educationmanager.misc.Application;
import com.comp3004.educationmanager.observer.CourseData;
import com.comp3004.educationmanager.observer.SystemData;
import com.comp3004.educationmanager.strategy.AddDocumentStrategy;
import com.comp3004.educationmanager.strategy.CourseContentStrategy;
import com.comp3004.educationmanager.strategy.SubmitDeliverableStrategy;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.MediaType;

import javax.annotation.PostConstruct;
import javax.print.attribute.standard.Media;
import java.io.*;
import java.util.*;

@RestController
public class Routes {
    // For things like converting Objects to json
    @Autowired
    ServerState s;
    StudentCreator studentCreator = new StudentCreator();
    ProfessorCreator professorCreator = new ProfessorCreator();
    //meant for subject usage, to notify the admin
    SystemData data = new SystemData();

    @PostConstruct
    public void initializeAdmin() {
        AdminCreator factory = new AdminCreator();
        Admin admin = (Admin) factory.createUser("admin", "pass");
        if (s.createUser(admin)) {
            System.out.println("admin user added to the db");
            data.attach(admin);
        } else {
            System.out.println("admin has failed to be created");
        }
        s.print();
    }

    @GetMapping("/api/members")
    public String members() {
        return "Cameron, Cameron, Ben, Jaxson";
    }

    @PostMapping(value ="/api/register", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String register(@RequestBody String info) {
        System.out.println("From '/api/register': " + info);
        HashMap<String, Object> map = Helper.stringToMap(info);
        map.replace("firstname", ((String) map.get("firstname")).toLowerCase());
        map.replace("lastname", ((String) map.get("lastname")).toLowerCase());
        //have this route create an application for the admin to validate

        //add this to the admin's list of applications
        Application a = new Application((String) map.get("firstname"), (String) map.get("lastname"), (String) map.get("password"), (String) map.get("type"));
        SystemData.admin.addApplication(a);
        //notify the admin
        data.updateAll("application", a);

        //send back that this user needs to wait to be approved
//        User newUser = studentCreator.createUser((String) map.get("firstname") + map.get("lastname"), (String) map.get("password"));
//
//        Student student = (Student) newUser;
//        student.addPastCourse("COMP2804");
//        student.addPastCourse("COMP2803");
//        student.addPastCourse("COMP3203");
//
//        s.createUser(newUser);
//        s.print();
        return info + " has attempted to be registered... waiting for permission from the admin";
    }

    @PostMapping(value ="/api/register-professor", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String registerProfessor(@RequestBody String info) {
        System.out.println("From '/api/register': " + info);
        HashMap<String, Object> map = Helper.stringToMap(info);
        map.replace("firstname", ((String) map.get("firstname")).toLowerCase());
        map.replace("lastname", ((String) map.get("lastname")).toLowerCase());
        //this is the notification to be added to the admin's list of notifications -- likely to be a part of the database, but for now I just want to get it all working
        User newUser = professorCreator.createUser((String) map.get("firstname") + map.get("lastname"), (String) map.get("password"));

        s.createUser(newUser);
        s.print();
        return info + " has attempted to be registered";
    }

    @PostMapping(value = "/api/login", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String login(@RequestBody String userinfo) {
        System.out.println("From '/api/login': " + userinfo);
        HashMap<String, Object> map = Helper.stringToMap(userinfo);
        //using this userinfo, see if there is a user with this information (auth)
        String answer = "";
        System.out.println(map.get("username"));
        System.out.println(map.get("password"));
        try {
            if (s.auth((String) map.get("username"), (String) map.get("password"))) {
                User ur = s.getUser((String) map.get("username"));
                String rs = Helper.objectToJSONString(ur);
                HashMap<String, Object> mm = Helper.stringToMap(rs);
                String type = ur.getClass().toString();
                int startIndex = type.indexOf(".accounts.") + ".accounts.".length();
                mm.put("type", type.substring(startIndex));
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


    /*
       Route for creating a course
       USAGE:
       @param (courseInfo JSON)
           - courseCode: courseCode of course, includes section (Ex. COMP3004B)
           - courseName: Name of course (Ex. Object-Oriented Software Engineering)
           - maxStudents: Maximum students allowed in course
           - prerequisites: List of courses that are prerequisites for the course being taken
           - professorID: ID of professor to be assigned to class
       @return Status of course creation (Success / Failure)
       */
    @PostMapping(value ="/api/create-course", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String createCourse(@RequestBody String courseInfo) throws IOException, ClassNotFoundException {
        System.out.println("From '/api/create-course': " + courseInfo);


        //HashMap <String, String> courseMap = Helper.stringToMap(courseInfo);

        //Creating HashMap of data sent in request

        Map<String, Object> courseMap = Helper.stringToMap(courseInfo);

        CourseData courseData = new CourseCreator().createCourse((String) courseMap.get("courseCode"), (String) courseMap.get("courseName") , (Integer) courseMap.get("maxStudents"));

        String courseCode = String.valueOf(courseMap.get("courseCode"));

        long professorID =Long.valueOf((Integer) courseMap.get("professorID")).longValue();


        User user = SystemData.users.get(professorID); //Retrieving User (The Professor) from List of Users
        //Removing [ and ] from String of coursecodes and converting that String to array
        ArrayList<String> coursePrerequisitesArray = (ArrayList<String>) courseMap.get("prerequisites");

        //Making sure to not add empty strings to prerequisite array (Only add if String has data)
        if (coursePrerequisitesArray.size() > 0) {
            for (String prerequisite : coursePrerequisitesArray) {
                courseData.addPrerequisite(prerequisite);
            }
        }


        Professor professor = (Professor) user; //Casting Professor to User
        courseData.attach(professor); //Attaching Professor to CourseData


        SystemData.courses.put(courseCode, courseData); //Storing CourseData in courses hashmap

        return courseInfo + " has been created";
    }

    /*
      Route for deleting a course
      USAGE:
      @param (courseInfo JSON)
          - courseCode: courseCode of course, includes section (Ex. COMP3004B)
      @return Status of course deletion (Success / Failure, should always succeed though)
      */
    @PostMapping(value ="/api/delete-course", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String deleteCourse(@RequestBody String courseInfo) throws IOException, ClassNotFoundException {
        System.out.println("From '/api/delete-course': " + courseInfo);

        //Needs to delete courses AND delete students / professors with course

        HashMap <String, String> courseMap = new ObjectMapper().readValue(courseInfo, HashMap.class);  //Creating HashMap of data sent in request

        String courseCode = courseMap.get("courseCode");

        //Calling updateAll with command deleteCourse on all observers for courseData
        //This will remove the course from the course list stored within the class
        SystemData.courses.get(courseCode).updateAll("deleteCourse", courseCode);

        //Removing course from list of courses
        SystemData.courses.remove(courseCode);

        return courseInfo + " has been deleted";
    }

    /*
     Route for student registering in course
     USAGE:
     @param (courseInfo JSON)
         - courseCode: courseCode of course, includes section (Ex. COMP3004B)
         - studentID: ID of student wanting to register in course
         -
     @return Status of course deletion (Success / Failure, should always succeed though)

     */
    @PostMapping(value ="/api/course-registration", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String courseRegistration(@RequestBody String studentInfo) throws IOException {
        System.out.println("From '/api/course-registration': " + studentInfo);

        //Needs to delete courses AND delete students / professors with course

        HashMap <String, String> infoMap = new ObjectMapper().readValue(studentInfo, HashMap.class);  //Creating HashMap of data sent in request

        CourseData courseData = SystemData.courses.get(infoMap.get("courseCode")); //Retrieving Course from list of courses

        long studentID = Long.parseLong(infoMap.get("studentID"));

        User user = SystemData.users.get(studentID); //Retrieving User (The Student Registering) From List of Users

        Student student = (Student) user; //Casting the User object to student

        //If student does not meet prerequisites let user know
        if (!student.doesStudentMeetPrerequisites(courseData.getPrerequisites())) {
            return "Student could not be registered in course as they do not meet prerequisites";
        } //If course is full, let user know that they could not be registered
        else if (courseData.isCourseFull()) {
            return "Student could not be registered in course as it is full";
        }
        //If date is past course registration date then do not let student register and let them know registration has closed
        else if (s.date.compareTo(s.lastRegistrationDate) >= 0) {
            return "Student could not be registered in course as course registration has closed";
        }
        else {
            courseData.attach(student);//Attaching Student to CourseData
            return studentInfo + " registered in course " + courseData.getCourseCode();
        }

    }

    @PostMapping(value ="/api/course-withdrawal", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String courseWithdrawal(@RequestBody String studentInfo) throws IOException {
        System.out.println("From '/api/course-withdrawl': " + studentInfo);

        HashMap <String, String> infoMap = new ObjectMapper().readValue(studentInfo, HashMap.class);  //Creating HashMap of data sent in request

        CourseData courseData = SystemData.courses.get(infoMap.get("courseCode")); //Retrieving Course from list of courses

        User user = SystemData.users.get(infoMap.get("studentID")); //Retrieving User (The Student Registering) From List of Users

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
    public String getUserCourses(@RequestBody String userInfo) {
        System.out.println("From '/api/get-user-courses: " + userInfo);

        HashMap<String, Object> userMap = Helper.stringToMap(userInfo);
        HashMap<String, CourseData> courseMap = new HashMap<>();

        Object id = userMap.get("studentID");
        if(id == null) {
            id = userMap.get("professorID");
        }
        User user = SystemData.users.get(id);
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

        return Helper.objectToJSONString(contentStrings);
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
        HashMap<String, Object> contentMap = Helper.stringToMap(contentInfo);
        CourseData course = s.getCourseData((String) contentMap.get("courseCode"));
        course.setStrategy(new CourseContentStrategy());
        Component comp = course.addContent((String) contentMap.get("name"), (String) contentMap.get("path"), (String) contentMap.get("type"));

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
        HashMap<String, Object> contentMap = Helper.stringToMap(contentInfo);
        CourseData course = s.getCourseData((String) contentMap.get("courseCode"));
        course.setStrategy(new SubmitDeliverableStrategy());
        Component comp = course.addContent((String) contentMap.get("name"), (String) contentMap.get("path"), (String) contentMap.get("type"), false);
        comp.setProperty("type", contentMap.get("type"));

        return (String) comp.getProperty("fullPath");
    }

    /*
    Route for adding a document
    USAGE:  Must convert the file to a byte array and then encode that as a string (with Base64 encoding)
            to be passed in with the contentInfo param
        - this will decode the String into a byte array and pass that to the file decorator
    @params (contentInfo JSON)
        - courseCode: course to add document to
        - bytes: byte array (as string) that stores the file data
        - name: name of document to add
        - path: path of where to add document
        - type: one of PDF, DOCX, PPTX
    TODO:
        -
     */
    @PostMapping(value = "/api/add-document", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String addDocument(@RequestBody String contentInfo) {
        HashMap<String, Object> contentMap = Helper.stringToMap(contentInfo);
        CourseData course = s.getCourseData((String) contentMap.get("courseCode"));

        String sBytes = (String) contentMap.get("bytes");
        System.out.println("BYTES: " + sBytes);
        byte[] bytes = Base64.getDecoder().decode(sBytes);

        course.setStrategy(new AddDocumentStrategy());
        Component comp = course.addContent((String) contentMap.get("name"), (String) contentMap.get("path"), (String) contentMap.get("type"));
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
        HashMap<String, Object> contentMap = Helper.stringToMap(contentInfo);


        return contentInfo + " has been submitted";
    }

    /*
    Route for submitting a deliverable grade(s) for a student(s)
    TODO:
        -
    */
    @PostMapping(value = "/api/add-grade", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String addGrade(@RequestBody String contentInfo) {
        HashMap<String, Object> contentMap = Helper.stringToMap(contentInfo);
        CourseData course = s.getCourseData((String) contentMap.get("courseCode"));
        Component c = (Component) course.getContent().executeCommand("findByPath", contentMap.get("path"));
        c.executeCommand("addGrade", Integer.parseInt((String) contentMap.get("grade")));

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
        HashMap<String, Object> contentMap = Helper.stringToMap(contentInfo);

        return contentInfo + " has been submitted";
    }

    /*
    Route for downloading a file
    TODO:
        -
     */
    @GetMapping(value = "/api/download-file", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] downloadFile(@RequestBody String contentInfo) {
        HashMap<String, Object> contentMap = Helper.stringToMap(contentInfo);
        CourseData course = s.getCourseData((String) contentMap.get("courseCode"));
        Component c = (Component) course.getContent().executeCommand("findByPath", contentMap.get("path"));
        return (byte[]) c.executeCommand("download", null);
    }

    /*
    Route for view a file as PDF
    TODO:
        - Must still implement conversion feature within the FileDecorator class
     */
    @GetMapping(value = "/api/view-file", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.ALL_VALUE)
    public byte[] viewFile(@RequestBody String contentInfo) {
        HashMap<String, Object> contentMap = Helper.stringToMap(contentInfo);
        CourseData course = s.getCourseData((String) contentMap.get("courseCode"));
        Component c = (Component) course.getContent().executeCommand("findByPath", contentMap.get("path"));
        return (byte[]) c.executeCommand("viewAsPDF", null);
    }

    @GetMapping(value = "/api/get-applications", produces = MediaType.TEXT_HTML_VALUE)
    public String getApplications() {
        String list = Helper.objectToJSONString(SystemData.admin.getApplications());
        System.out.println("Sending: " + list);
        return list;
    }
    /*
        This method takes in two fields: name (firstname + " " + lastname) and type: "Student"/"Professor"
        It is meant to create a user from this application information if it can find the application currently pending
        It then removes this application and updates the admin via web socket to get the new list of applications
     */
    @PostMapping(value = "/api/process-application", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String processApplication(@RequestBody String applicationInfo) {
        //find this application in the admin's list of applications
        System.out.println(applicationInfo);
        HashMap<String, Object> appinfo = Helper.stringToMap(applicationInfo);
        String[] names = ((String) appinfo.get("name")).split(" ");
        ArrayList<Application> pending = SystemData.admin.getApplications();
        for(int i = 0; i < pending.size(); i++) {
            if(names[0].equals(pending.get(i).getFirstname()) && names[1].equals(pending.get(i).getLastname()) && appinfo.get("type").equals(pending.get(i).getType())) {
                //this is the application we are looking for. Create a user from this application, and end the request
                Application thisApplication = SystemData.admin.getApplications().get(i);
                s.createUserFromApplication(thisApplication);
                SystemData.admin.getApplications().remove(i);
                data.updateAll("application", thisApplication);
                return "success";
            }
        }
        //if we reach the end of the for loop, we know that the application with this information was not found. Return an error
        return "error";
    }

    /*
        This method takes in two fields: name (firstname + " " + lastname) and type: "Student"/"Professor"
        It serves to find this application with these parameters and delete them from the list of applications in the admin
        It then tells the admin via web socket that it needs to update its list of applications.
     */
    @PostMapping(value = "/api/delete-application", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String deleteApplication(@RequestBody String applicationInfo) {
        //find this application in the admin's list of applications
        HashMap<String, Object> appinfo = Helper.stringToMap(applicationInfo);
        String[] names = ((String) appinfo.get("name")).split(" ");
        ArrayList<Application> pending = SystemData.admin.getApplications();
        for(int i = 0; i < pending.size(); i++) {
            if(names[0].equals(pending.get(i).getFirstname()) && names[1].equals(pending.get(i).getLastname()) && appinfo.get("type").equals(pending.get(i).getType())) {
                //we want to delete this application and end the request.
                SystemData.admin.getApplications().remove(i);
                data.updateAll("application", null);
                return "success";
            }
        }
        //if we reach this part of the code, then we were unable to find this application
        return "error";
    }
}



