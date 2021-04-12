package com.comp3004.educationmanager;

import com.comp3004.educationmanager.misc.Serialization;
import com.comp3004.educationmanager.observer.SystemData;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Server.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerTest {
    private String sendRequest(String type, String apiRoute, String params) {
        try {
            URL url = new URL("http://localhost:8080/api/" + apiRoute);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(type);

            boolean canWrite = true;
            if(type.equals("GET")) {
                canWrite = false;
            }
            http.setDoOutput(canWrite);

            http.setRequestProperty("Content-Type", "text/html");

            if(canWrite) {
                http.setFixedLengthStreamingMode(params.length());
                OutputStream os = http.getOutputStream();
                os.write(params.getBytes(StandardCharsets.UTF_8));
                os.flush();
                os.close();
            }

            int status = http.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));

            StringBuffer response = new StringBuffer();
            String input = reader.readLine();
            while (input != null) {
                response.append(input);
                input = reader.readLine();
            }
            reader.close();
            http.disconnect();
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    @Order(1)
    public void testServerInitialization() {
        String response = sendRequest("GET", "members", "");
        assertNotNull(response);

        System.out.println("Received members: " + response);
        System.out.println("Server is successfully online");
    }

    @Test
    @Order(2)
    public void testSystemInitialization() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userID", "0");

        HashMap<String, Object> response = Helper.stringToMap(sendRequest("POST", "get-user", Helper.objectToJSONString(map)));
        assertEquals("admin", response.get("name"));
        System.out.println("Admin and system have been successfully initialized");
    }

    @Test
    @Order(3)
    public void testAdminLogin() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", "admin");
        map.put("password", "pass");

        HashMap<String, Object> response = Helper.stringToMap(sendRequest("POST", "login", Helper.objectToJSONString(map)));
        assertEquals("admin", response.get("name"));
    }

    @Test
    @Order(4)
    public void testErrorLogin() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", "bad");
        map.put("password", "login");

        HashMap<String, Object> response = Helper.stringToMap(sendRequest("POST", "login", Helper.objectToJSONString(map)));
        assertNotNull(response.get("error"));
    }

    @Test
    @Order(5)
    public void testProfessorRegistration() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("firstname", "robert");
        map.put("lastname", "collier");
        map.put("password", "password");
        map.put("type", "professor");

        HashMap<String, Object> response = Helper.stringToMap(sendRequest("POST", "register", Helper.objectToJSONString(map)));
        assertEquals("robert", response.get("firstname"));
        assertEquals("professor", response.get("type"));
    }

    @Test
    @Order(6)
    public void testProcessProfessorApplication() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "robert collier");
        map.put("type", "professor");

        sendRequest("POST", "process-application", Helper.objectToJSONString(map));

        map.clear();
        map.put("userID", "2000000");

        HashMap<String, Object> response = Helper.stringToMap(sendRequest("POST", "get-user", Helper.objectToJSONString(map)));
        assertEquals("robertcollier", response.get("name"));
        assertNotNull(response.get("professorID"));
        System.out.println("Professor has been successfully added to the system");
    }

    @Test
    @Order(7)
    public void testProfessorLogin() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", "robertcollier");
        map.put("password", "password");

        HashMap<String, Object> response = Helper.stringToMap(sendRequest("POST", "login", Helper.objectToJSONString(map)));
        assertEquals("robertcollier", response.get("username"));
    }

    @Test
    @Order(8)
    public void testCourseCreation() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("courseCode", "COMP1405A");
        map.put("courseName", "Introduction to Computer Science");
        map.put("maxStudents", "200");
        map.put("prerequisites", new ArrayList<String>() {});
        map.put("professorID", "2000000");
        map.put("days", new ArrayList<>(Arrays.asList("monday", "wednesday")));
        map.put("startTime", "11:35");
        map.put("classDuration", "1.5");

        sendRequest("POST", "create-course", Helper.objectToJSONString(map));

        map.clear();
        map.put("courseCode", "COMP1405A");
        HashMap<String, Object> response = Helper.stringToMap(sendRequest("POST", "get-course-info", Helper.objectToJSONString(map)));
        assertEquals("COMP1405A", response.get("courseCode"));
        assertEquals("Introduction to Computer Science", response.get("courseName"));

        map.clear();
        response.clear();
        map.put("userID", "2000000");
        response = Helper.stringToMap(sendRequest("POST", "get-user", Helper.objectToJSONString(map)));
        ArrayList<String> courses = (ArrayList) response.get("courses");
        System.out.println(courses);
    }

    @Test
    @Order(9)
    public void testStudentRegistration() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("firstname", "ben");
        map.put("lastname", "williams");
        map.put("password", "compscirocks");
        map.put("type", "student");

        HashMap<String, Object> response = Helper.stringToMap(sendRequest("POST", "register", Helper.objectToJSONString(map)));
        assertEquals("ben", response.get("firstname"));
        assertEquals("student", response.get("type"));
    }

    @Test
    @Order(10)
    public void testProcessStudentApplication() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "ben williams");
        map.put("type", "student");

        sendRequest("POST", "process-application", Helper.objectToJSONString(map));

        map.clear();
        map.put("userID", "1000000");

        HashMap<String, Object> response = Helper.stringToMap(sendRequest("POST", "get-user", Helper.objectToJSONString(map)));
        assertEquals("benwilliams", response.get("name"));
        assertNotNull(response.get("studentID"));
        System.out.println("Student has been successfully added to the system");
    }

    @Test
    @Order(11)
    public void testStudentLogin() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", "benwilliams");
        map.put("password", "compscirocks");

        HashMap<String, Object> response = Helper.stringToMap(sendRequest("POST", "login", Helper.objectToJSONString(map)));
        assertEquals("benwilliams", response.get("username"));
    }

    @Test
    @Order(12)
    public void testCourseRegistration() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("courseCode", "COMP1405A");
        map.put("studentID", 1000000);

        HashMap<String, Object> response = Helper.stringToMap(sendRequest("POST", "course-registration", Helper.objectToJSONString(map)));
        assertNotNull(response.get("success"));

        map.clear();
        map.put("userID", "1000000");
        response = Helper.stringToMap(sendRequest("POST", "get-user", Helper.objectToJSONString(map)));
        assertEquals(1, ((ArrayList) response.get("courses")).size());
        assertEquals(0, ((ArrayList) response.get("pastCourses")).size());
    }

    @Test
    @Order(13)
    public void testCourseCreationWithPrereqs() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("courseCode", "COMP2402A");
        map.put("courseName", "Abstract Data Types");
        map.put("maxStudents", "125");
        map.put("prerequisites", new ArrayList<String>(Arrays.asList("COMP1405A")));
        map.put("professorID", "2000000");
        map.put("days", new ArrayList<>(Arrays.asList("tuesday", "thursday")));
        map.put("startTime", "11:35");
        map.put("classDuration", "1.5");

        sendRequest("POST", "create-course", Helper.objectToJSONString(map));

        map.clear();
        map.put("courseCode", "COMP2402A");
        HashMap<String, Object> response = Helper.stringToMap(sendRequest("POST", "get-course-info", Helper.objectToJSONString(map)));
        assertEquals("COMP2402A", response.get("courseCode"));
        assertEquals("Abstract Data Types", response.get("courseName"));
    }

    @Test
    @Order(14)
    public void testErrorCourseRegistrationWithPrereqs() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("courseCode", "COMP2402A");
        map.put("studentID", 1000000);

        HashMap<String, Object> response = Helper.stringToMap(sendRequest("POST", "course-registration", Helper.objectToJSONString(map)));
        assertNotNull(response.get("error"));

        map.clear();
        map.put("userID", "1000000");
        response = Helper.stringToMap(sendRequest("POST", "get-user", Helper.objectToJSONString(map)));
        assertEquals(1, ((ArrayList) response.get("courses")).size());
        assertEquals(0, ((ArrayList) response.get("pastCourses")).size());
    }

    @Test
    @Order(15)
    public void testCreateStudentWithPrereqs() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("firstname", "cameron");
        map.put("lastname", "rolfe");
        map.put("password", "secondyear");
        map.put("prerequisites", new ArrayList<>(Arrays.asList("COMP1405A")));

        HashMap<String, Object> response = Helper.stringToMap(sendRequest("POST", "register-user-prerequisites", Helper.objectToJSONString(map)));
        assertNotNull(response.get("success"));

        map.clear();
        map.put("userID", "1000001");
        response = Helper.stringToMap(sendRequest("POST", "get-user", Helper.objectToJSONString(map)));
        assertEquals("cameronrolfe", response.get("name"));
        assertEquals(1, ((ArrayList) response.get("pastCourses")).size()); // NOTE: user starts with a placeholder course, this is removed once they register in their first course
        assertEquals(1, ((ArrayList) response.get("pastCourses")).size());
    }

    @Test
    @Order(16)
    public void testCourseRegistrationWithPrereqs() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("courseCode", "COMP2402A");
        map.put("studentID", 1000001);

        HashMap<String, Object> response = Helper.stringToMap(sendRequest("POST", "course-registration", Helper.objectToJSONString(map)));
        assertNotNull(response.get("success"));

        map.clear();
        map.put("userID", "1000001");
        response = Helper.stringToMap(sendRequest("POST", "get-user", Helper.objectToJSONString(map)));
        assertEquals(1, ((ArrayList) response.get("courses")).size());
        assertEquals(1, ((ArrayList) response.get("pastCourses")).size());
    }

    @Test
    @Order(17)
    public void testErrorTimeslotCourseCreation() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("courseCode", "COMP1805A");
        map.put("courseName", "Discrete Structures");
        map.put("maxStudents", "150");
        map.put("prerequisites", new ArrayList<String>());
        map.put("professorID", "2000000");
        map.put("days", new ArrayList<>(Arrays.asList("monday", "tuesday")));
        map.put("startTime", "11:35");
        map.put("classDuration", "1.5");

        HashMap<String, Object> response = Helper.stringToMap(sendRequest("POST", "create-course", Helper.objectToJSONString(map)));
        assertNotNull(response.get("error"));
    }

    @Test
    @Order(18)
    public void testSecondProfessorCreation() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("firstname", "jean-pierre");
        map.put("lastname", "corriveau");
        map.put("password", "designdesigndesign");
        map.put("type", "professor");

        sendRequest("POST", "register", Helper.objectToJSONString(map));

        map.clear();
        map.put("name", "jean-pierre corriveau");
        map.put("type", "professor");
        sendRequest("POST", "process-application", Helper.objectToJSONString(map));

        map.clear();
        map.put("userID", "2000001");

        HashMap<String, Object> response = Helper.stringToMap(sendRequest("POST", "get-user", Helper.objectToJSONString(map)));
        assertEquals("jean-pierrecorriveau", response.get("name"));
        assertNotNull(response.get("professorID"));
    }

    @Test
    @Order(19)
    public void testThirdCourseCreation() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("courseCode", "COMP3004B");
        map.put("courseName", "Design Patterns");
        map.put("maxStudents", "100");
        map.put("prerequisites", new ArrayList<String>());
        map.put("professorID", "2000001");
        map.put("days", new ArrayList<>(Arrays.asList("monday", "wednesday")));
        map.put("startTime", "11:35");
        map.put("classDuration", "1.5");

        sendRequest("POST", "create-course", Helper.objectToJSONString(map));

        map.clear();
        map.put("courseCode", "COMP3004B");
        HashMap<String, Object> response = Helper.stringToMap(sendRequest("POST", "get-course-info", Helper.objectToJSONString(map)));
        assertEquals("COMP3004B", response.get("courseCode"));
        assertEquals("Design Patterns", response.get("courseName"));
    }

    @Test
    @Order(20)
    public void testErrorTimeslotCourseRegistration() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("courseCode", "COMP3004B");
        map.put("studentID", 1000000);

        HashMap<String, Object> response = Helper.stringToMap(sendRequest("POST", "course-registration", Helper.objectToJSONString(map)));
        assertNotNull(response.get("error"));
    }

    @Test
    @Order(22)
    public void testPastDeadlineCourseRegistration() {

    }

    @Test
    @Order(23)
    public void testAddContentToCourse() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("courseCode", "COMP3004B");
        map.put("name", "Section 1");
        map.put("path", "/COMP3004B/");
        map.put("type", "section");
        map.put("userID", "2000001");
        map.put("userType", "professor");
        map.put("visible", "true");

        HashMap<String, Object> response = Helper.stringToMap(sendRequest("POST", "add-content", Helper.objectToJSONString(map)));
        response = (HashMap) response.get("wrappee");
        response = (HashMap) ((ArrayList) response.get("children")).get(0);
        response = (HashMap) response.get("wrappee");
        assertEquals("Section 1", response.get("name"));
    }

    @Test
    @Order(24)
    public void testAddDocumentToCourse() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("courseCode", "COMP3004B");
        map.put("name", "Document 1");
        map.put("path", "/COMP3004B/Section 1/");
        map.put("type", "PDF");
        map.put("userID", "2000001");
        map.put("userType", "professor");
        map.put("visible", "true");
        map.put("bytes", "SGVsbG8gV29ybGQ=");

        HashMap<String, Object> response = Helper.stringToMap(sendRequest("POST", "add-document", Helper.objectToJSONString(map)));
        System.out.println(response);
        response = (HashMap) response.get("wrappee");
        response = (HashMap) ((ArrayList) response.get("children")).get(0);
        response = (HashMap) response.get("wrappee");
        response = (HashMap) ((ArrayList) response.get("children")).get(0);
        response = (HashMap) response.get("wrappee");

        HashMap<String, Object> file = (HashMap) response.get("file");
        //assertEquals("Hello World", new String((byte[]) file.get("bytes")));

        response = (HashMap) response.get("wrappee");
        assertEquals("Document 1", response.get("name"));
    }

    @Test
    public void testAddDeliverableToCourse() {

    }

    @Test
    public void testSubmitDeliverable() {

    }

    @Test
    public void testModifyDeliverable() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("courseCode", "COMP3004B");
        map.put("name", "Section 1");
        map.put("path", "/COMP3004B/");
        map.put("type", "section");
        map.put("userID", "2000001");
        map.put("userType", "professor");
        map.put("visible", "true");
        map.put("bytes", "R29vZGJ5ZSBXb3JsZA==");

        HashMap<String, Object> response = Helper.stringToMap(sendRequest("POST", "add-document", Helper.objectToJSONString(map)));
        response = (HashMap) response.get("wrappee");
        response = (HashMap) ((ArrayList) response.get("children")).get(0);
        response = (HashMap) response.get("wrappee");
        response = (HashMap) ((ArrayList) response.get("children")).get(0);
        response = (HashMap) response.get("wrappee");



        HashMap<String, Object> file = (HashMap) response.get("file");
        //assertEquals("Goodbye World", Base64.getDecoder().decode((String) file.get("byteString")).toString());

        response = (HashMap) response.get("wrappee");
        assertEquals("Document 1", response.get("name"));
    }

    @Test
    public void testPastDeadlineSubmitDeliverable() {
        // have two separate students submit deliverables?
    }

    @Test
    public void testDeleteContentFromCourse() {

    }

    @Test
    public void testAddDeliverableGrade() {

    }

    @Test
    public void testSubmitFinalGrade() {

    }

    @Test
    public void testDownloadDocument() {

    }

    @Test
    public void testViewDocumentAsPDF() {
        // currently not working for PPTX
    }

    @Test
    public void testDeleteCourse() {

    }

    @Test
    public void testCourseWithdrawal() {

    }

    @Test
    public void testLastCourseWithdrawal() {
        // verify student was deleted from system
    }

    @Test
    public void testPastDeadlineCourseWithdrawal() {

    }
}
