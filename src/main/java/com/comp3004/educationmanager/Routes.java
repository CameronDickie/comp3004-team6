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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.view.RedirectView;

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
                HashMap<String, String> errorMessage = new HashMap<>();
                errorMessage.put("error", "login failed");
                answer = Helper.objectToJSONString(errorMessage);
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
           - courseCode: courseCode of course, includes section (Ex. COMP3004B)         (String)
           - courseName: Name of course (Ex. Object-Oriented Software Engineering)      (String)
           - maxStudents: Maximum students allowed in course                            (Int)
           - prerequisites: List of courses that are prerequisites for the course being taken   (ArrayList<String>)
           - professorID: ID of professor to be assigned to class                               (Int)
           - days: The days the course takes place      (ArrayList <String>)
           - startTime: Start time of class in form of "12:30"      (String)
           - classDuration: Total amount of time the classes takes in hours     (Int)
       @return Status of course creation
       */
    @PostMapping(value ="/api/create-course", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String createCourse(@RequestBody String courseInfo) throws IOException, ClassNotFoundException {
        System.out.println("From '/api/create-course': " + courseInfo);


        //HashMap <String, String> courseMap = Helper.stringToMap(courseInfo);

        //Creating HashMap of data sent in request

        Map<String, Object> courseMap = Helper.stringToMap(courseInfo);


         CourseData courseData = new CourseCreator().createCourse(
                (String) courseMap.get("courseCode"),
                (String) courseMap.get("courseName"),
                Integer.parseInt((String) courseMap.get("maxStudents")),
                (ArrayList<String>) courseMap.get("days"),
                (String) courseMap.get("startTime"),
                Double.parseDouble((String) courseMap.get("classDuration")),
                (ArrayList<String>) courseMap.get("prerequisites"));

        String courseCode = String.valueOf(courseMap.get("courseCode"));
        long professorID =Long.parseLong((String) courseMap.get("professorID"));

        User user = SystemData.users.get(professorID); //Retrieving User (The Professor) from List of Users
        Professor professor = (Professor) user; //Casting Professor to User
        courseData.attach(professor); //Attaching Professor to CourseData

        SystemData.courses.put(courseCode, courseData); //Storing CourseData in courses hashmap

        professor.addCourse(courseData); //giving this professor the course in their list of courses
        //inform the admin user that their list of courses must be updated
        data.updateAll("get-courses", courseData);
        //inform all users associated with this course (currently just the professor) that they need to update their courses
        courseData.updateAll("get-courses", courseData);

         String jsonReturn = "{success:'";
        jsonReturn+= courseCode + " has been created'}";
        return jsonReturn;
    }

    /*
      Route for deleting a course
      USAGE:
      @param (courseInfo JSON)
          - courseCode: courseCode of course, includes section (Ex. COMP3004B)      (String)
      @return Status of course deletion
      */
    @PostMapping(value ="/api/delete-course", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String deleteCourse(@RequestBody String courseInfo) throws IOException, ClassNotFoundException {
        System.out.println("From '/api/delete-course': " + courseInfo);

        //Needs to delete courses AND delete students / professors with course

        HashMap <String, Object> courseMap = Helper.stringToMap(courseInfo);  //Creating HashMap of data sent in request

        String courseCode = (String) courseMap.get("courseCode");

        //Calling updateAll with command deleteCourse on all observers for courseData
        //This will remove the course from the course list stored within the class
        SystemData.courses.get(courseCode).updateAll("deleteCourse", courseCode);

        //Removing course from list of courses
        SystemData.courses.remove(courseCode);

        String jsonReturn = "{success:'";
        jsonReturn+= courseCode + " has been deleted'}";
        return jsonReturn;
    }

   /*
     Route for student registering in course
     USAGE:
     @param (courseInfo JSON)
         - courseCode: courseCode of course, includes section (Ex. COMP3004B)   (String)
         - studentID: ID of student wanting to register in course               (Integer)
     @return Status of course registration

     */
    @PostMapping(value ="/api/course-registration", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String courseRegistration(@RequestBody String studentInfo) throws IOException {
        System.out.println("From '/api/course-registration': " + studentInfo);

        HashMap <String, Object> infoMap = Helper.stringToMap(studentInfo);  //Creating HashMap of data sent in request

        CourseData courseData = SystemData.courses.get(infoMap.get("courseCode")); //Retrieving Course from list of courses

        long studentID = Long.valueOf((Integer) infoMap.get("studentID"));

        User user = SystemData.users.get(studentID); //Retrieving User (The Student Registering) From List of Users

        Student student = (Student) user; //Casting the User object to student

        int studentRegistrationStatus = student.canStudentRegisterInCourse(courseData, s.date, s.deadline);

        String jsonReturn = "{error:'";
        //0 = Student can register successfully
        //1 = Course Registration Closed
        //2 = Course is Full
        //3 = Student does not meet prerequisites
        //4 = Timetable Conflicts
        if (studentRegistrationStatus == 0) {
            courseData.attach(student);
            courseData.updateAll("get-courses", courseData);
//            student.update("addCourse", courseData);
            jsonReturn = "{success:'";
            jsonReturn+= "Student has successfully registered in course " + courseData.getCourseCode() + "'}";
        }
        else if (studentRegistrationStatus == 1) {
            jsonReturn+= "Student could not be registered in course as course registration has closed'}";
        }
        else if (studentRegistrationStatus == 2) {
            jsonReturn+= "Student could not be registered in course as it is full'}";
        }
        else if (studentRegistrationStatus == 3) {
            jsonReturn+= "Student could not be registered in course as they do not meet prerequisites'}";
        }
        else if (studentRegistrationStatus == 4) {
            jsonReturn+= "Student could not be registered in course as there are timetable conflicts'}";
        }



        return jsonReturn;
    }

    /*
     Route for student withdrawing from course
     USAGE:
     @param (courseInfo JSON)
         - courseCode: courseCode of course, includes section (Ex. COMP3004B)   (String)
         - studentID: ID of student wanting to register in course               (Integer)
     @return Status of course widthdrawl

     */
    @PostMapping(value ="/api/course-withdrawal", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String courseWithdrawal(@RequestBody String studentInfo) throws IOException {
        System.out.println("From '/api/course-withdrawal': " + studentInfo);

        HashMap <String, Object> infoMap = Helper.stringToMap(studentInfo);  //Creating HashMap of data sent in request

        CourseData courseData = SystemData.courses.get(infoMap.get("courseCode")); //Retrieving Course from list of courses

        long studentID = Long.valueOf((Integer) infoMap.get("studentID"));

        User user = SystemData.users.get(studentID); //Retrieving User (The Student Registering) From List of Users

        Student student = (Student) user; //Casting the User object to student

        if (student.canStudentWithdraw(s.date, s.deadline)) {
            courseData.detach(student);//Detach student from course
            student.update("deleteCourse", courseData.getCourseCode());
            System.out.println(student.getCourses());
            String jsonReturn = "{success:'Student has successfully withdrawn from the course " + courseData.getCourseCode() + "'}";
            return jsonReturn;
        }
        else {
            courseData.attach(student);
            String jsonReturn = "{error:'Student cannot withdraw as the withdraw deadline has past " + courseData.getCourseCode() + "'}";
            return jsonReturn;
        }
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

    @PostMapping(value="/api/get-user-courses-minimal", consumes=MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String getUserCoursesMinimal(@RequestBody String userInfo) {
        HashMap<String, Object> userMap = Helper.stringToMap(userInfo);
        //get the user in SystemData with this information
        User curUser = null;
        for(User u : SystemData.users.values()) {
            if(userMap.get("username").equals(u.getName()) && Long.valueOf((Integer)userMap.get("id"))==u.getUserId()) {
                //we know this is the user we are interested in for their courses
                curUser = u;
                break;
            }
        }
        //if curUser is still null, we need to see if the information matches the admin's
        if(curUser == null) {
            if(userMap.get("username").equals(SystemData.admin.getName())) {
                //we know this is the admin.
                curUser = SystemData.admin;
            }
        }
        if(curUser == null) {
            //if the current user is still null, we need to return an error as we could not find this user
            HashMap<String, String> errMap = new HashMap<>();
            errMap.put("error", "unable to find the provided user");
            return Helper.objectToJSONString(errMap);
        }
        //we now have the user, and must collect all of their courses. if it is the admin, return a list of all courses in the system
        ArrayList<HashMap<String, String>> courseInfo = new ArrayList<>();
        if(curUser instanceof Admin) {
            // return a list of all courses' information
            for(CourseData c : SystemData.courses.values()) {
                //append a string with courseCode, courseName and id to the courseInfo string
                HashMap<String, String> thisCourseInfo = new HashMap<String, String>();
                thisCourseInfo.put("code", c.getCourseCode());
                thisCourseInfo.put("name", c.getCourseName());
                thisCourseInfo.put("id", String.valueOf(c.getCourseID()));
                courseInfo.add(thisCourseInfo);
            }
            return Helper.objectToJSONString(courseInfo);
        } else if(curUser instanceof Student) {
            //casting this into a student to access their courses attribute
            Student s = (Student) curUser;
            //getting relevant information about the courses for this user
            for(CourseData c : s.getCourses().values()) {
                HashMap<String, String> thisCourseInfo = new HashMap<String, String>();
                thisCourseInfo.put("code", c.getCourseCode());
                thisCourseInfo.put("name", c.getCourseName());
                thisCourseInfo.put("id", String.valueOf(c.getCourseID()));
                courseInfo.add(thisCourseInfo);
            }
            return Helper.objectToJSONString(courseInfo);
        } else if(curUser instanceof Professor) {
            //casting this into a professor to access their courses attribute
            Professor p = (Professor) curUser;
            //getting relevant information about the courses for this user
            for(CourseData c : p.getCourses().values()) {
                HashMap<String, String> thisCourseInfo = new HashMap<String, String>();
                thisCourseInfo.put("code", c.getCourseCode());
                thisCourseInfo.put("name", c.getCourseName());
                thisCourseInfo.put("id", String.valueOf(c.getCourseID()));
                courseInfo.add(thisCourseInfo);
            }
            return Helper.objectToJSONString(courseInfo);
        }
        //if we have not returned at this point, we must return an error message
        HashMap<String, String> errMsg = new HashMap<>();
        errMsg.put("error", "unable to query this users' courses");
        return Helper.objectToJSONString(errMsg);
    }

    @PostMapping(value="/api/get-global-courses", consumes=MediaType.TEXT_HTML_VALUE, produces=MediaType.TEXT_HTML_VALUE)
    public String getGlobalCourses(@RequestBody String userInfo) {
        //this function is meant to provide a student with a list of courses from which they can register in
        //we must filter the list of global courses with courses that they are already registered in

        HashMap<String, Object> userMap = Helper.stringToMap(userInfo);
        User curUser = null;
        //find the user with this information
        for(User u : SystemData.users.values()) {
            if(userMap.get("username").equals(u.getName()) && Long.valueOf((Integer)userMap.get("id"))==u.getUserId()) {
                //this is the student we are looking for
                curUser = u;
                break;
            }
        }
        if(curUser == null) {
            System.out.println("Error finding the user with this information");
            HashMap<String, String> err = new HashMap<>();
            err.put("error", "Could not find the user with this information");
            return Helper.objectToJSONString(err);
        }
        if(curUser instanceof Student) {
            Student curStudent = (Student) curUser;
            ArrayList<HashMap<String, String>> global = new ArrayList<>();
            //search through all of the available courses and if curStudent.courses does not contain this, add it to a new hashmap
            for(CourseData c : SystemData.courses.values()) {
                boolean alreadyRegistered = false;
                for(CourseData sc: curStudent.getCourses().values()) {
                    if(c.getCourseID() == sc.getCourseID()) {
                        alreadyRegistered = true;
                    }
                }
                if(!alreadyRegistered) {
                    HashMap<String, String> thisCourseInfo = new HashMap<String, String>();
                    thisCourseInfo.put("code", c.getCourseCode());
                    thisCourseInfo.put("name", c.getCourseName());
                    thisCourseInfo.put("id", String.valueOf(c.getCourseID()));
                    global.add(thisCourseInfo);
                }
            }
            return Helper.objectToJSONString(global);
        } else if(curUser instanceof Professor) {
            //we do not need to see if this professor is registed in a course, as they will not be registering from here
            ArrayList<HashMap<String, String>> global = new ArrayList<>();

            for(CourseData c : SystemData.courses.values()) {
                HashMap<String, String> thisCourseInfo = new HashMap<>();
                thisCourseInfo.put("code", c.getCourseCode());
                thisCourseInfo.put("name", c.getCourseName());
                thisCourseInfo.put("id", String.valueOf(c.getCourseID()));
                global.add(thisCourseInfo);
            }

            return Helper.objectToJSONString(global);
        } else {
            //this user is not a student or a professor... return an error
            HashMap<String, String> err = new HashMap<>();
            err.put("error", "Could not recognize the type of user sending the information");
            return Helper.objectToJSONString(err);
        }

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
                User newUser = s.createUserFromApplication(thisApplication);
                SystemData.admin.getApplications().remove(i);
                if(newUser == null) {
                    System.out.println("error creating user");
                    HashMap<String, String> err = new HashMap<>();
                    err.put("error", "unable to create the user from this application");
                    return Helper.objectToJSONString(err);
                }
                if(newUser instanceof Student) {
                    SystemData.users.put(((Student) newUser).getStudentID(), newUser);
                } else if(newUser instanceof Professor) {
                    SystemData.users.put(((Professor) newUser).getProfessorID(), newUser);
                } else {
                    System.out.println("error inserting user");
                    HashMap<String, String> err = new HashMap<>();
                    err.put("error", "unable to create the user from this application and put them in the list of users... maybe this user already exists?");
                    return Helper.objectToJSONString(err);
                }
                data.updateAll("application", thisApplication);
                if(appinfo.get("type").equals("professor")) {
                    data.updateAll("get-professor", null);
                }
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

    @GetMapping(value = "/api/get-all-professors", produces = MediaType.TEXT_HTML_VALUE)
    public String getAllProfessors() {
        //return a list of all users that are of type professor in systemdata
        ArrayList<HashMap<String, String>> profs = new ArrayList<>();
        for(User u : SystemData.users.values()) {
            if(u instanceof Professor) {
//                profs.add((Professor) u);
                HashMap<String, String> profInfo = new HashMap<>();
                profInfo.put("name", u.getName());
                profInfo.put("id", String.valueOf(((Professor) u).getProfessorID()));
                profs.add(profInfo);
            }
        }
        return Helper.objectToJSONString(profs);
    }
    /*
    TODO:
        ABSOLUTELY NOTHING. PLEASE DO NOT MODIFY THESE FUNCTIONS FOR ABSOLUTELY ANY REASON. THIS WILL CAUSE SUDDEN HEART FAILURE.
     */
    @GetMapping(value="/admin")
    public RedirectView adminRedirect() {
        return new RedirectView("/");
    }
    @GetMapping(value="/dashboard")
    public RedirectView dashboardRedirect() {
        return new RedirectView("/");
    }
    @GetMapping(value="/signup")
    public RedirectView signupRedirect() {
        return new RedirectView("/");
    }

}



