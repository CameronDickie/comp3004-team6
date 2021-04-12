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
import com.comp3004.educationmanager.strategy.AddDeliverableStrategy;
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

        //initialize a base course that cannot be registered in, but holds all students that have not yet registered in a course
        CourseData emptyCourse = new CourseData();
        SystemData.courses.put(emptyCourse.getCourseCode(), emptyCourse);
        s.print();
    }

    /*
    FOR TESTING
     */

    @PostMapping(value ="/api/get-user", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String getUser(@RequestBody String info) {
        System.out.println("From '/api/get-user': " + info);
        HashMap<String, Object> map = Helper.stringToMap(info);

        User user = data.users.get(Long.parseLong((String) map.get("userID")));

        if (user == null) {
            user = data.admin;
        }

        HashMap<String, Object> response = new HashMap<>();
        response.put("userID", user.getUserId());
        response.put("name", user.getName());
        response.put("password", user.getPassword());
        if(user instanceof Student) {
            Student s = (Student) user;
            response.put("studentID", s.getStudentID());
            response.put("courses", s.getCourses().keySet());
            response.put("pastCourses", s.getPastCourses());
        } else if(user instanceof Professor) {
            Professor p = (Professor) user;
            response.put("professorID", p.getProfessorID());
            response.put("courses", p.getCourses().keySet());
        }

        return Helper.objectToJSONString(response);
    }

    @PostMapping(value ="/api/get-course-info", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String getCourseInfo(@RequestBody String info) {
        System.out.println("From '/api/get-course-info': " + info);
        HashMap<String, Object> map = Helper.stringToMap(info);

        CourseData data = s.getCourseData((String) map.get("courseCode"));
        HashMap<String, Object> response = new HashMap<>();
        response.put("courseCode", data.getCourseCode());
        response.put("courseName", data.getCourseName());
        response.put("maxStudents", data.getMaxStudents());
        response.put("prerequisites", data.getPrerequisites());
        response.put("days", data.getDays());
        response.put("startTime", data.getStartTime());
        response.put("classDuration", data.getClassDuration());
        response.put("professorID", data.getProfessor().getProfessorID());

        return Helper.objectToJSONString(response);
    }

    @PostMapping(value ="/api/set-system-date", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String setSystemDate(@RequestBody String info) {
        HashMap<String, Object> map = Helper.stringToMap(info);
        String[] date = ((String) map.get("date")).split("-");
        s.date.set(Calendar.YEAR, Integer.parseInt(date[0]));
        s.date.set(Calendar.MONTH, Integer.parseInt(date[1]) -1);
        s.date.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[2]));
        s.date.set(Calendar.HOUR_OF_DAY, Integer.parseInt(date[3]));
        s.date.set(Calendar.MINUTE, Integer.parseInt(date[4]));

        HashMap<String, Object> response = new HashMap<>();
        response.put("success", "set time to " + map.get("date"));
        return Helper.objectToJSONString(response);
    }
    /*
    END OF FOR TESTING
     */


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
        return info + " has attempted to be registered... waiting for permission from the admin";
    }

    /*
    Allows the admin to force the creation of a new user with provided prerequisites
    Parameters
        (String) firstname: The first name of the new user,
        (String) lastname: The last name of the new user,
        (String) password: The password to be used by this new user,
        (ArrayList<String>): A list of the courseCodes that will be the past courses of this user
        (String) type: The type of user to be created
     */
    @PostMapping(value="/api/register-user-prerequisites", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String registerWithPrerequisites(@RequestBody String userInfo) {

        HashMap<String, Object> userMap = Helper.stringToMap(userInfo);
        if(userMap.get("firstname") == null || userMap.get("lastname") == null || userMap.get("password") == null || userMap.get("type") == null) {
            HashMap<String, String> response = new HashMap<>();
            response.put("error", "user is missing required fields");
            return Helper.objectToJSONString(response);
        }
        if(userMap.get("type").equals("student")) {
            Student st = (Student) studentCreator.createUser(((String) userMap.get("firstname")).toLowerCase() + ((String) userMap.get("lastname")).toLowerCase(), (String) userMap.get("password"));
            ArrayList<String> thisUserPastCourses = (ArrayList<String>) userMap.get("prerequisites");

            if(thisUserPastCourses.size() > 0) {
                //add all of the past courses courseCode to this student
                for(String cid : thisUserPastCourses) {
                    //ensure that the course indeed exists
                    if(!SystemData.courses.containsKey(cid)) {
                        System.out.println("Unable to add the course " + cid + " as we could not find it in the system");
                    } else {
                        st.addPastCourse(cid);
                    }
                }
            }
            //attach this new user to the placeholder course
            CourseData placeholder = SystemData.courses.get("COUR1234A");
            placeholder.attach(st);

            //add this user to the system
            s.createUser(st);
            HashMap<String, String> response = new HashMap<>();
            response.put("success", "user has been added");
            return Helper.objectToJSONString(response);
        } else if(userMap.get("type").equals("professor")) {
            //create a professor with this information
            Professor p = (Professor) professorCreator.createUser(((String) userMap.get("firstname")).toLowerCase() + ((String) userMap.get("lastname")).toLowerCase(), (String) userMap.get("password"));

            //attach this new user to the placeholder course
            CourseData placeholder = SystemData.courses.get("COUR1234A");
            placeholder.attach(p);

            s.createUser(p);

            HashMap<String, String> response = new HashMap<>();
            response.put("success", "user has been added");
            return Helper.objectToJSONString(response);
        } else {
            HashMap<String, String> response = new HashMap<>();
            response.put("error", "user is not of type student or professor!");
            return Helper.objectToJSONString(response);
        }

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
        long professorID = Long.parseLong((String) courseMap.get("professorID"));


        User user = SystemData.users.get(professorID); //Retrieving User (The Professor) from List of Users
        Professor professor = (Professor) user; //Casting Professor to User

        if (professor.canProfessorBeAssignedToCourse(courseData)) {
            courseData.attach(professor); //Attaching Professor to CourseData
            CourseData placeholder = professor.getCourses().get("COUR1234A");
            if(placeholder != null) {
                professor.getCourses().remove("COUR1234A");
            }
            SystemData.courses.put(courseCode, courseData); //Storing CourseData in courses hashmap

            professor.addCourse(courseData); //giving this professor the course in their list of courses
            //inform the admin user that their list of courses must be updated
            data.updateAll("get-courses", courseData);
            //inform all users associated with this course (currently just the professor) that they need to update their courses
            courseData.updateAll("get-courses", courseData);

            //inform all users in the system that they need to update their list of global courses (viewed in registration)
            for (User u : SystemData.users.values()) {
                u.update("get-global-courses", courseData);
            }

            String jsonReturn = "{\"success\":\"";
            jsonReturn += courseCode + " has been created\"}";
            return jsonReturn;
        } else {
            String jsonReturn = "{\"error\":\"";
            jsonReturn += courseCode + " could not be created as Professor has timetable conflict\"}";
            return jsonReturn;
        }


    }



    /*
      Route for deleting a course
      TODO:
        Proper integration with the users and have the course be delete if there have been users and no more users exist in the course
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
        data.updateAll("get-courses", courseCode);
        //Removing course from list of courses
        SystemData.courses.remove(courseCode);

        String jsonReturn = "{\"success\":\"";
        jsonReturn+= courseCode + " has been deleted\"}";
        return jsonReturn;
    }

   /*
     Route for student registering in course
     TODO:
        Ensure edge cases of the front end data do not break any of the components
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

        String jsonReturn = "{\"error\":\"";
        //0 = Student can register successfully
        //1 = Course Registration Closed
        //2 = Course is Full
        //3 = Student does not meet prerequisites
        //4 = Timetable Conflicts
        if (studentRegistrationStatus == 0) {
            courseData.attach(student);
            courseData.updateAll("get-courses", courseData);

            //if this user has the placeholder course, remove it
            CourseData placeholder = student.getCourses().get("COUR1234A");
            if(placeholder != null) {
                //remove this course
                student.getCourses().remove("COUR1234A");
            }
            jsonReturn = "{\"success\":\"";
            jsonReturn+= "Student has successfully registered in course " + courseData.getCourseCode() + "\"}";
        }
        else if (studentRegistrationStatus == 1) {
            jsonReturn+= "Student could not be registered in course as course registration has closed\"}";
        }
        else if (studentRegistrationStatus == 2) {
            jsonReturn+= "Student could not be registered in course as it is full\"}";
        }
        else if (studentRegistrationStatus == 3) {
            jsonReturn+= "Student could not be registered in course as they do not meet prerequisites\"}";
        }
        else if (studentRegistrationStatus == 4) {
            jsonReturn+= "Student could not be registered in course as there are timetable conflicts\"}";
        }



        return jsonReturn;
    }

    /*
     Route for student withdrawing from course
     TODO:
        test this usage on the front-end.
        Provide users an option to call this route when they open the 'courseContent' page on the front end
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
//            courseData.updateAll("get-courses", student.getStudentID()); //inform all other users that they need to
            student.update("get-courses", student.getStudentID());
            System.out.println(student.getCourses());
            //if student.getCourses() is null, remove the user
            if(student.getCourses().size() == 0) {
                //remove this student
                SystemData.users.remove(student.getStudentID());
                student.update("removal-from-system", student);
            }

            String jsonReturn = "{\"success\":\"Student has successfully withdrawn from the course " + courseData.getCourseCode() + "\"}";
            return jsonReturn;
        }
        else {
//            courseData.attach(student);
            String jsonReturn = "{\"error\":\"Student cannot withdraw as the withdraw deadline has past " + courseData.getCourseCode() + "\"}";
            return jsonReturn;
        }
    }
    /*
    Route for getting a student's courses
    USAGE:
    @param (userInfo JSON)
        - courseCode (String): code of course content to retrieve
    @return a stringified version of the course
    TODO:
        find a use for this lol
    */
    @PostMapping(value = "/api/get-course", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String getCourseContent(@RequestBody String courseInfo) {
        System.out.println("From '/api/get-course: " + courseInfo);
        HashMap<String, Object> courseMap = Helper.stringToMap(courseInfo);
        CourseData course = s.getCourseData((String) courseMap.get("courseCode"));
        return (String) course.getContent().executeCommand("stringify", null);
    }

    /*
    should be good haven't had issues with it and is being used at every level of admin, student, and professor dashboards
     */
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
                if(c.getCourseCode().equals("COUR1234A")) {
                    //ignore placeholder course(s)
                    continue;
                }
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
                if(c.getCourseCode().equals("COUR1234A")) {
                    //ignore placeholder course(s)
                    continue;
                }
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
                if(c.getCourseCode().equals("COUR1234A")) {
                    //ignore placeholder course(s)
                    continue;
                }
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

    /*
    TODO:
        ensure that the user information is being handled properly on every request. So far, no request has broken this.
     */
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
                if(c.getCourseCode().equals("COUR1234A")) {
                    //ignore placeholder course(s)
                    continue;
                }
                boolean alreadyRegistered = false;
                for(CourseData sc: curStudent.getCourses().values()) {
                    if(c.getCourseID().equals(sc.getCourseID())) {
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
                if(c.getCourseCode().equals("COUR1234A")) {
                    //ignore placeholder course(s)
                    continue;
                }
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
    Route for adding/modifying a course content object
    USAGE:  Call this when the professor wants to create content items such as course sections or lectures, use a separate post request to attach any documents
    @param (contentInfo JSON)
        - courseCode (String): the course to add the content to
        - name (String): the name for the CourseContent object
        - path (String): the path for the CourseContent object (i.e. /COMP3004B/)
        - type (String): the type of composite object being added (one of: lecture, section)
        - userID (Long): id of the user creating the content
        - userType (String): is the user a student or professor
        - visible (boolean): whether or not the item is visible to students
    @return the stringifed version of the course
    */
    @PostMapping(value = "/api/add-content", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String addContent(@RequestBody String contentInfo) {
        HashMap<String, Object> contentMap = Helper.stringToMap(contentInfo);
        CourseData course = s.getCourseData((String) contentMap.get("courseCode"));
        course.setStrategy(new CourseContentStrategy());
        String info = (String) contentMap.get("path") + (String) contentMap.get("name") + "/";
        Component comp = (Component) course.getContent().executeCommand("findByPath", info);
        if(comp != null) {
            for(Map.Entry<String, Object> entry : contentMap.entrySet()) {
                comp.setProperty(entry.getKey(), entry.getValue());
            }
        } else {
            comp = course.addContent((String) contentMap.get("name"),
                    (String) contentMap.get("path"),
                    (String) contentMap.get("type"),
                    Long.parseLong((String) contentMap.get("userID")),
                    (String) contentMap.get("userType"),
                    Boolean.parseBoolean((String) contentMap.get("visible")));
        }
        course.updateAll("get-course-content", null);
        return (String) course.getContent().executeCommand("stringify", null);
    }

    /*
    Route for adding/modifying a document
    USAGE:  Must convert the file to a byte array and then encode that as a string (with Base64 encoding)
            to be passed in with the contentInfo param
        - this will decode the String into a byte array and pass that to the file decorator
    @params (contentInfo JSON)
        - courseCode (String): course to add document to
        - name (String): name of document to add
        - path (String): path of where to add document
        - type (String): one of PDF, DOCX, PPTX
        - userID (long): id of the user adding the document
        - userType (String): type of the user adding a document
        - visible (boolean): whether the item is visible to students
        - bytes (String): read the file into a byte array and encode as string using Base64 encoding
    @return the stringified version of the course
    TODO:
     */
    @PostMapping(value = "/api/add-document", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String addDocument(@RequestBody String contentInfo) {
        HashMap<String, Object> contentMap = Helper.stringToMap(contentInfo);
        CourseData course = s.getCourseData((String) contentMap.get("courseCode"));
        course.setStrategy(new AddDocumentStrategy());
        String info = (String) contentMap.get("path") + (String) contentMap.get("name") + "/";
        Component comp = (Component) course.getContent().executeCommand("findByPath", info);
        if(comp != null) {
            for(Map.Entry<String, Object> entry : contentMap.entrySet()) {
                if(entry.getKey().equals("userID")) {
                    comp.setProperty(entry.getKey(), Long.parseLong((String) entry.getValue()));
                } else if(entry.getKey().equals("visible")) {
                    comp.setProperty(entry.getKey(), Boolean.parseBoolean((String) entry.getValue()));
                } else {
                    comp.setProperty(entry.getKey(), entry.getValue());
                }

            }
        } else {
            comp = course.addContent((String) contentMap.get("name"),
                    (String) contentMap.get("path"),
                    (String) contentMap.get("type"),
                    Long.parseLong((String) contentMap.get("userID")),
                    (String) contentMap.get("userType"),
                    Boolean.parseBoolean((String) contentMap.get("visible")));
        }

        String bytes = (String) contentMap.get("bytes");
        comp.setProperty("file", bytes);
        course.updateAll("get-course-content", null);
        return (String) course.getContent().executeCommand("stringify", null);
    }

    /*
    Route for adding/modifying a course deliverable
    USAGE: Call this when the professor wants to create a course deliverable, use a separate post request to attach documents as children
    @params
        - courseCode (String): the course to add the deliverable to
        - name (String): the name for the CourseContent object
        - path (String): the path for the CourseContent object (i.e. /COMP3004B/)
        - type (String): the type of deliverable object being added (one of: quiz, assignment)
        - userID (Long): id of the user creating the content
        - userType (String): is the user a student or professor
        - visible (boolean): whether or not the item is visible to students
        - deadline (String): deadline for the deliverable (YYYY-MM-DD-hh-mm)
    @return the stringified version of the course
    TODO:
     */
    @PostMapping(value = "/api/add-deliverable", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String addDeliverable(@RequestBody String contentInfo) {
        HashMap<String, Object> contentMap = Helper.stringToMap(contentInfo);
        CourseData course = s.getCourseData((String) contentMap.get("courseCode"));
        course.setStrategy(new AddDeliverableStrategy());
        String info = (String) contentMap.get("path") + (String) contentMap.get("name") + "/";
        Component comp = (Component) course.getContent().executeCommand("findByPath", info);
        if(comp != null) {
            for(Map.Entry<String, Object> entry : contentMap.entrySet()) {
                comp.setProperty(entry.getKey(), entry.getValue());
            }
        } else {
            comp = course.addContent((String) contentMap.get("name"),
                    (String) contentMap.get("path"),
                    (String) contentMap.get("type"),
                    Long.parseLong((String) contentMap.get("userID")),
                    (String) contentMap.get("userType"),
                    Boolean.parseBoolean((String) contentMap.get("visible")));
        }

        comp.setProperty("deadline", (String) contentMap.get("deadline"));
        course.updateAll("get-course-content", null);
        return (String) course.getContent().executeCommand("stringify", null);
    }

    /*
    Route for submitting/modifying a course deliverable
    USAGE: Call this when the student wants to submit a course deliverable, use a separate post request to attach any documents
    @params
        - courseCode (String): the course to add the deliverable to
        - name (String): the name for the CourseContent object
        - path (String): the path for the CourseContent object (i.e. /COMP3004B/)
        - type (String): the type of deliverable object being added (one of: quiz, assignment)
        - userID (Long): id of the user creating the content
        - userType (String): is the user a student or professor
        - visible (boolean): don't need to pass this in, defaults to false
        - bytes (String): byte array (encoded as string)
    @return the stringified version of the course or an error message
    TODO:
     */
    @PostMapping(value = "/api/submit-deliverable", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String submitDeliverable(@RequestBody String contentInfo) {
        HashMap<String, Object> contentMap = Helper.stringToMap(contentInfo);
        CourseData course = s.getCourseData((String) contentMap.get("courseCode"));
        course.setStrategy(new SubmitDeliverableStrategy());
        String path = (String) contentMap.get("path");
//        String pathToDeliverable = path.substring(0, path.lastIndexOf("/"));
//        pathToDeliverable = pathToDeliverable.substring(0, path.lastIndexOf("/"));
        Component deliverable = (Component) course.getContent().executeCommand("findByPath", path);
        if((boolean) deliverable.executeCommand("isBeforeDeadline", s.date)) {
            String info = (String) contentMap.get("path") + (String) contentMap.get("name") + "/";
            Component comp = (Component) course.getContent().executeCommand("findByPath", info);
            if(comp != null) {
                for(Map.Entry<String, Object> entry : contentMap.entrySet()) {
                    comp.setProperty(entry.getKey(), entry.getValue());
                }
            } else {
                comp = course.addContent( (String) contentMap.get("name"),
                        (String) contentMap.get("path"),
                        (String) contentMap.get("type"),
                        Long.parseLong((String) contentMap.get("userID")),
                        (String) contentMap.get("userType"),
                        false);
                comp.setProperty("file", (String) contentMap.get("bytes"));
            }

            course.updateAll("get-course-content", null);
            return (String) course.getContent().executeCommand("stringify", null);
        } else {
            HashMap<String, String> err = new HashMap<>();
            err.put("error", "Deadline has passed");
            return Helper.objectToJSONString(err);
        }
    }

    /*
    Route for deleting course content
    @params
        - courseCode (String): the course to add the deliverable to
        - path (String): the path for the CourseContent object (i.e. /COMP3004B/)
    @return the stringified version of the course
    TODO:
     */
    @PostMapping(value = "/api/delete-content", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String deleteContent(@RequestBody String contentInfo) {
        HashMap<String, Object> contentMap = Helper.stringToMap(contentInfo);
        CourseData course = s.getCourseData((String) contentMap.get("courseCode"));
        course.getContent().executeCommand("delete-item", (String) contentMap.get("path"));
        course.updateAll("get-course-content", null);
        return (String) course.getContent().executeCommand("stringify", null);
    }

    /*
    Route for submitting a deliverable grade(s) for a student(s)
    USAGE: call when adding a grade to a student's deliverable
    @params
        - courseCode (String): code of the course where the assignment is held
        - path (String): path to the student submission
        - grade (float): grade to be added
    TODO: implement socket updates?
    */
    @PostMapping(value = "/api/add-grade", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String addGrade(@RequestBody String contentInfo) {
        HashMap<String, Object> contentMap = Helper.stringToMap(contentInfo);
        CourseData course = s.getCourseData((String) contentMap.get("courseCode"));
        Component c = (Component) course.getContent().executeCommand("findByPath", contentMap.get("path"));
        if(c != null) {
            c.setProperty("grade", Float.parseFloat((String) contentMap.get("grade")));
            HashMap<String, String> suc = new HashMap<>();
            suc.put("success", "Grade added properly");
            course.updateAll("get-course-content", null);
            return Helper.objectToJSONString(suc);
        } else {
            HashMap<String, String> err = new HashMap<>();
            err.put("error", "Can't find user submission");
            return Helper.objectToJSONString(err);
        }
    }

    /*
    Route for submitting the final grade for a student(s)
    @param
        - studentID (long): the student to add the grade to
        - courseCode (String): the course that the grade relates to
        - grade (int): the final grade that the student received
    @return failure/success message
    TODO: implement socket updates? (already using update method so just add socket stuff to the finalGrade command)
     */
    @PostMapping(value = "/api/submit-final-grade", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String submitFinalGrade(@RequestBody String contentInfo) {
        HashMap<String, Object> contentMap = Helper.stringToMap(contentInfo);
        Student student = (Student) SystemData.users.get((String) contentMap.get("studentID"));
        if(student != null) {
            HashMap<String, Integer> gradeMap = new HashMap<>();
            gradeMap.put((String) contentMap.get("courseCode"), Integer.parseInt((String) contentMap.get("grade")));
            student.update("finalGrade", gradeMap);

            HashMap<String, String> suc = new HashMap<>();
            suc.put("success", "Grade added properly");
            return Helper.objectToJSONString(suc);
        } else {
            HashMap<String, String> err = new HashMap<>();
            err.put("error", "Can't find user");
            return Helper.objectToJSONString(err);
        }
    }

    /*
    Route for downloading a file
    @param
        - courseCode (String): code for the course in which the document is stored
        - path (String): path to the document
    @return Base64 encoded string holding the file
    TODO:
     */
    @GetMapping(value = "/api/download-file", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String downloadFile(@RequestBody String contentInfo) {
        HashMap<String, Object> contentMap = Helper.stringToMap(contentInfo);
        CourseData course = s.getCourseData((String) contentMap.get("courseCode"));
        Component c = (Component) course.getContent().executeCommand("findByPath", contentMap.get("path"));
        if(c != null) return (String) c.executeCommand("download", null);
        else {
            HashMap<String, String> err = new HashMap<>();
            err.put("error", "Can't find file");
            return Helper.objectToJSONString(err);
        }
    }

    /*
    Route to convert/view a file as a PDF
    @param
        - courseCode (String): code for the course in which the document is stored
        - path (String): path to the document
    @return Base64 encoded string holding the converted file
    TODO: fix PPTX to PDF conversion in FileViewVisitor class (route is finished, will cause issues on the conversion though)
     */
    @GetMapping(value = "/api/view-file", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String viewFile(@RequestBody String contentInfo) {
        HashMap<String, Object> contentMap = Helper.stringToMap(contentInfo);
        CourseData course = s.getCourseData((String) contentMap.get("courseCode"));
        Component c = (Component) course.getContent().executeCommand("findByPath", contentMap.get("path"));
        if(c != null) return (String) c.executeCommand("viewAsPDF", null);
        else {
            HashMap<String, String> err = new HashMap<>();
            err.put("error", "Can't find file");
            return Helper.objectToJSONString(err);
        }
    }

    /*
    Seems to work so far
     */
    @GetMapping(value = "/api/get-applications", produces = MediaType.TEXT_HTML_VALUE)
    public String getApplications() {
        String list = Helper.objectToJSONString(SystemData.admin.getApplications());
        System.out.println("Sending: " + list);
        return list;
    }
    /*
        This method takes in two fields: name (firstname + " " + lastname) and type: "student"/"professor"
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

                    //insert this user into the placeholder course
                    CourseData placeholder = SystemData.courses.get("COUR1234A");
                    placeholder.attach((Student) newUser);
                    ((Student) newUser).addCourse(placeholder);
                    SystemData.courses.replace("COUR1234A", placeholder);

                } else if(newUser instanceof Professor) {
                    SystemData.users.put(((Professor) newUser).getProfessorID(), newUser);

                    //insert this user into the placeholder course
                    CourseData placeholder = SystemData.courses.get("COUR1234A");
                    placeholder.attach((Professor) newUser);
                    ((Professor) newUser).addCourse(placeholder);
                    SystemData.courses.replace("COUR1234A", placeholder);
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
        Working as expected
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

    /*
    Returns the list of all professors within the system, with attributes {name, id}
     */
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



