package com.comp3004.educationmanager;

import com.comp3004.educationmanager.accounts.Admin;
import com.comp3004.educationmanager.accounts.Professor;
import com.comp3004.educationmanager.accounts.Student;
import com.comp3004.educationmanager.accounts.User;
import com.comp3004.educationmanager.composite.Component;
import com.comp3004.educationmanager.composite.CourseItem;
import com.comp3004.educationmanager.decorator.FileDecorator;
import com.comp3004.educationmanager.factory.AdminCreator;
import com.comp3004.educationmanager.factory.CourseCreator;
import com.comp3004.educationmanager.factory.ProfessorCreator;
import com.comp3004.educationmanager.factory.StudentCreator;
import com.comp3004.educationmanager.observer.CourseData;
import com.comp3004.educationmanager.observer.SystemData;
import com.comp3004.educationmanager.strategy.*;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SystemFunctionsTest {
    CourseCreator cc = new CourseCreator();
    ProfessorCreator pc = new ProfessorCreator();
    StudentCreator sc = new StudentCreator();
    AdminCreator ac = new AdminCreator();

    static SystemData system;
    static CourseData course;
    static Professor professor;
    static Student student;

    //Basic System Creation Test
    @Test
    public void testSystemCreation() {
        system = new SystemData();

        User admin = ac.createUser("admin", "pass");
        assertNotNull(admin);

        system.attach(admin);
        assertTrue(system.getObservers().size() > 0);
    }


    /////////////////////////////////////////////////// STRATEGY / DECORATOR / COMPOSITE TESTS  //////////////////////////////////////////


    //Test that verifies that following:
    //- Base Strategy CourseContentStrategy used to create course component
    //- Adding component to course works
    //- component.executeCommand(findByPath) function works to find component after adding it
    //- component.executeCommand(deleteItem)function works to delete a component based on path
    //- component.executeCommand(findByPath) function returns null when trying to find deleted component
    @Test
    public void testAddAndDeleteCourseContent() {
        Long userID = Long.valueOf(2000000);
        String userType = "Professor";
        boolean visible = true;

        ArrayList<String> prerequisites = new ArrayList<>();
        ArrayList<String> days = new ArrayList<>();
        days.add("Tuesday");
        days.add("Thursday");

        CourseData courseData = cc.createCourse("COMP3004A", "Object-Oriented Software Engineering", 100, days, "12:30", 1.5, prerequisites);

        courseData.setStrategy(new CourseContentStrategy());

        courseData.addContent("Lecture 1", "/COMP3004A/", "Lecture", userID, userType, visible);

        Component component = (Component) courseData.getContent().executeCommand("findByPath", "/COMP3004A/Lecture 1/");

        assertNotNull(component);

        courseData.getContent().executeCommand("delete-item", "/COMP3004A/Lecture 1/");

        Component component2 = (Component) courseData.getContent().executeCommand("findByPath", "/COMP3004A/Lecture 1/");

        assertNull(component2);
    }

    //Test that verifies that following:
    //- Testing Strategy AddDeliverableStrategy
    //- component.setProperty and component.getProperty for deadline
    //- component.executeCommand(isBeforeDeadline, date) returns true if date is before deadline
    //- component.executeCommand(isBeforeDeadline, date) returns false if date is after deadline
    @Test
    public void testAddDeliverableStrategy() {
        AddDeliverableStrategy strategy = new AddDeliverableStrategy();
        String name = "Assignment 1";
        String path = "/Assignment 1";
        String type = "Assignment";
        Long userID = Long.valueOf(2000000);
        String userType = "Professor";
        boolean visible = true;

        Component comp = strategy.createCourseItem(name, path, type, userID, userType, visible);

        //Setting Deadline to February 1st 2021
        comp.setProperty("deadline", "2021-02-01-00-00"); //Apparently everything is back one?

        assertNotNull(comp);
        assertEquals(comp.getProperty("name"), "Assignment 1");
        assertEquals(comp.getProperty("path"), "/Assignment 1");
        assertEquals(comp.getProperty("type"), "Assignment");
        assertTrue((boolean) comp.getProperty("visible"));
        assertNotNull(comp.getProperty("deadline"));
        assertNotNull(comp.getProperty("editable"));

        Calendar test = (Calendar) comp.getProperty("deadline");

        System.out.println(test.getTime());

        Calendar date = Calendar.getInstance();

        //Setting date to January 21st 2021 (Before Deadline)
        date.set(Calendar.MONTH, Calendar.JANUARY);
        date.set(Calendar.DAY_OF_MONTH, 21);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        System.out.println(date.DAY_OF_MONTH);

        assertTrue((Boolean) comp.executeCommand("isBeforeDeadline", date));

        //Setting date to February 2nd (After Deadline)
        date.set(Calendar.MONTH, Calendar.FEBRUARY);
        date.set(Calendar.DAY_OF_MONTH, 2);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        assertFalse((Boolean) comp.executeCommand("isBeforeDeadline", date));

        comp.setProperty("name", "Test 1");
        comp.setProperty("path", "/Test 1");
        comp.setProperty("type", "Test");
        assertEquals(comp.getProperty("name"), "Test 1");
        assertEquals(comp.getProperty("path"), "/Test 1");
        assertEquals(comp.getProperty("type"), "Test");
    }

    //Test that verifies that following:
    //- Testing Strategy AddDocumentStrategy
    //- component.setProperty for file
    //- component.executeCommand(download, null) returns file
    @Test
    public void testAddDocumentStrategy() {
        AddDocumentStrategy strategy = new AddDocumentStrategy();
        String name = "Assignment 1 Outline";
        String path = "/Assignment 1 Outline";
        String type = "PDF";
        Long userID = Long.valueOf(2000000);
        String userType = "Professor";
        boolean visible = true;

        Component comp = strategy.createCourseItem(name, path, type, userID, userType, visible);

        comp.setProperty("file", "randomByteString");
        assertEquals(comp.getProperty("file"), "randomByteString");

        assertEquals(comp.executeCommand("download", null), "randomByteString");
        assertNotNull(comp.executeCommand("viewAsPDF", null));

        assertNotNull(comp);
        assertEquals(comp.getProperty("name"), "Assignment 1 Outline");
        assertEquals(comp.getProperty("path"), "/Assignment 1 Outline");
        assertEquals(comp.getProperty("type"), "PDF");
        assertTrue((boolean) comp.getProperty("visible"));
        assertNotNull(comp.getProperty("editable"));
    }

    //Test that verifies that following:
    //- Testing Strategy SubmitDeliverableStrategy
    //- component.setProperty and component.getProperty for grade
    @Test
    public void testSubmitDeliverableStrategy() {
        SubmitDeliverableStrategy strategy = new SubmitDeliverableStrategy();
        String name = "Assignment 1";
        String path = "/Assignment 1";
        String type = "Deliverable";
        Long userID = Long.valueOf(1000000);
        String userType = "Student";
        boolean visible = true;

        Component comp = strategy.createCourseItem(name, path, type, userID, userType, visible);

        assertNotNull(comp.getProperty("grade"));

        comp.setProperty("grade", 91.23f);

        assertEquals(comp.getProperty("grade"), 91.23f);

        assertNotNull(comp);
        assertEquals(comp.getProperty("name"), "Assignment 1");
        assertEquals(comp.getProperty("path"), "/Assignment 1");
        assertEquals(comp.getProperty("type"), "Deliverable");
        assertTrue((boolean) comp.getProperty("visible"));
        assertNotNull(comp.getProperty("editable"));
    }



    /////////////////////////////////////////////////// FACTORY TESTS //////////////////////////////////////////

    //Student Creator Factory Test
    @Test
    public void testStudentCreatorFactory() {
        StudentCreator sc = new StudentCreator();

        Student student = (Student) sc.createUser("cameronrolfe", "password");

        assertNotNull(student);
    }

    //Professor Creator Factory Test
    @Test
    public void testProfessorCreatorFactory() {
        ProfessorCreator pc = new ProfessorCreator();

        Professor professor = (Professor) pc.createUser("cameronrolfe", "password");

        assertNotNull(professor);
    }

    //Admin Creator Factory Test
    @Test
    public void testAdminCreatorFactory() {
        AdminCreator ac = new AdminCreator();
        Admin admin = (Admin) ac.createUser("admin", "password");

        assertNotNull(admin);
    }

    //Course Creator Factory Test
    @Test
    public void testCourseCreatorFactory() {
        CourseCreator cc = new CourseCreator();

        ArrayList<String> days = new ArrayList<>();
        days.add("Tuesday");
        days.add("Thursday");

        ArrayList<String> prerequisites = new ArrayList<>();

        CourseData courseData = cc.createCourse("COMP3004A", "Object-Oriented Software Engineering", 100, days, "12:30", 1.5, prerequisites);

        assertNotNull(courseData);
    }

    /////////////////////////////////////////////////// OBSERVER TESTS //////////////////////////////////////////

    //Basic Observer Test that verifies that following:
    //- Using CourseData, verify that observers size is 0 before anything is attached
    //- Verify that attaching Professor to CourseData increase observer size
    //- Verify that attaching Student to CourseData increase observer size
    @Test
    public void basicObserverTest() {
        ArrayList<String> days = new ArrayList<>();
        days.add("Tuesday");
        days.add("Thursday");

        ArrayList<String> prerequisites = new ArrayList<>();

        CourseData courseData = cc.createCourse("COMP3004A", "Object-Oriented Software Engineering", 100, days, "12:30", 1.5, prerequisites);

        assertNotNull(courseData);

        Professor professor = (Professor) pc.createUser("JeanPierreCorriveau", "password");

        assertEquals(courseData.getObservers().size(), 0);

        courseData.attach(professor);

        assertEquals(courseData.getObservers().size(), 1);

        Student student = (Student) sc.createUser("cameronrolfe", "password");

        courseData.attach(student);

        assertEquals(courseData.getObservers().size(), 2);

    }


    //Intensive Observer Test:
    //Verifies the following:
    // - Create Course Data, attach students and professors and make sure they are added
    // - course.attach(student) adds course for student
    // - 4 Students Registered, asserts that course size is 4
    // - courseData.updateAll for "deleteCourse" removes course from professor and students
    @Test
    public void testCourseDeletionObserverUpdateAll() {
        ArrayList<String> days = new ArrayList<>();
        days.add("Tuesday");
        days.add("Thursday");

        ArrayList<String> prerequisites = new ArrayList<>();

        CourseData courseData = cc.createCourse("COMP3004A", "Object-Oriented Software Engineering", 100, days, "12:30", 1.5, prerequisites);

        assertNotNull(courseData);


        //Professor
        // - assert that course size is 0 and that course does not exist in their courses attribute before attach to course
        // - assert that after attach to course that the size of student course is now 1 and that the course does exist in their courses attribute
        Professor professor = (Professor) pc.createUser("JeanPierreCorriveau", "password");

        assertEquals(courseData.getObservers().size(), 0);
        assertEquals(professor.getCourses().size(), 0);
        assertNull(professor.getCourses().get(courseData.getCourseCode()));
        courseData.attach(professor);
        assertNotNull(professor.getCourses().get(courseData.getCourseCode()));
        assertEquals(courseData.getObservers().size(), 1);


        //For each student:
        // - assert that their course size is 0 and that course does not exist in their courses attribute before attach to course
        // - assert that after attach to course that the size of student course is now 1 and that the course does exist in their courses attribute

        //Student 1
        Student student = (Student) sc.createUser("cameronrolfe", "password");
        assertEquals(student.getCourses().size(), 0);
        assertNull(student.getCourses().get(courseData.getCourseCode()));
        courseData.attach(student);
        assertEquals(courseData.getObservers().size(), 2);
        assertEquals(student.getCourses().size(), 1);
        assertNotNull(student.getCourses().get(courseData.getCourseCode()));

        //Student 2
        Student student2 = (Student) sc.createUser("benwilliams", "password");
        assertEquals(student2.getCourses().size(), 0);
        assertNull(student2.getCourses().get(courseData.getCourseCode()));
        courseData.attach(student2);
        assertEquals(student2.getCourses().size(), 1);
        assertNotNull(student2.getCourses().get(courseData.getCourseCode()));

        //Student 3
        Student student3 = (Student) sc.createUser("jaxsonhood", "password");
        assertEquals(student3.getCourses().size(), 0);
        assertNull(student3.getCourses().get(courseData.getCourseCode()));
        courseData.attach(student3);
        assertEquals(student3.getCourses().size(), 1);
        assertNotNull(student3.getCourses().get(courseData.getCourseCode()));

        //Student 4
        Student student4 = (Student) sc.createUser("camerondickie", "password");
        assertNull(student4.getCourses().get(courseData.getCourseCode()));
        assertEquals(student4.getCourses().size(), 0);
        courseData.attach(student4);
        assertEquals(student4.getCourses().size(), 1);
        assertNotNull(student4.getCourses().get(courseData.getCourseCode()));


        //Asserting Course Has 4 Students
        assertEquals(courseData.getCurrStudents(), 4);

        //Deleting Course
        courseData.updateAll("deleteCourse", courseData.getCourseCode());

        //Verifying For Each Student that Course was Removed

        //Student #1
        assertEquals(student.getCourses().size(), 0);
        assertNull(student.getCourses().get(courseData.getCourseCode()));

        //Student #2
        assertEquals(student2.getCourses().size(), 0);
        assertNull(student2.getCourses().get(courseData.getCourseCode()));

        //Student #3
        assertEquals(student3.getCourses().size(), 0);
        assertNull(student3.getCourses().get(courseData.getCourseCode()));

        //Student #4
        assertEquals(student4.getCourses().size(), 0);
        assertNull(student4.getCourses().get(courseData.getCourseCode()));


        assertNull(professor.getCourses().get(courseData.getCourseCode()));

    }

    //Test for adding course content
    @Test
    public void testAddingContent() {
        ArrayList<String> days = new ArrayList<>();
        days.add("Tuesday");
        days.add("Thursday");
        ArrayList<String> prerequisites = new ArrayList<>();

        CourseData course = cc.createCourse("COMP3004A", "Object-Oriented Software Engineering", 100, days, "12:30", 1.5, prerequisites);

        course.setStrategy(new CourseContentStrategy());
        Component c = course.addContent("testContent", "/COMP3004A/", "section",2000000 ,  "Professor", true);

        course.setStrategy(new AddDocumentStrategy());
        c = course.addContent("testDoc", "/COMP3004A/testContent/", "DOCX", 2000000 ,  "Professor", true);
        String sBytes = "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";
        byte[] bytes = Base64.getDecoder().decode(sBytes);
        c.setProperty("file", sBytes);

        Component test = (Component) course.getContent().executeCommand("findByPath", "/COMP3004A/testContent/testDoc/");
        assertNotNull(test);

        String downloadBytes = (String) test.executeCommand("download", null);
        String viewBytes = (String) test.executeCommand("viewAsPDF", null);

        assertNotNull(downloadBytes);
        assertNotNull(viewBytes);
    }


    /////////////////////////////////////////////////// COURSE REGISTRATION TESTS //////////////////////////////////////////
    //RETURN FOR STUDENT COURSE REGISTRATION:
    //0 = Student can register
    //1 = Registration Date Past
    //2 = Course is Full
    //3 = Student does not meet course prerequisites
    //4 = Student has timetable conflict

    //Test to make sure student can register in a course when there are zero conflicts
    @Test
    public void testCourseRegistrationNoConflicts() {
        ArrayList<String> days = new ArrayList<>();
        days.add("Tuesday");
        days.add("Thursday");

        Calendar date = Calendar.getInstance();
        Calendar deadline = Calendar.getInstance();

        //Setting date to January 1st 2021 at 0:00
        date.set(Calendar.MONTH, Calendar.JANUARY);
        date.set(Calendar.DAY_OF_MONTH, 1);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        //Setting last registration date to random date of January 20th
        deadline.set(Calendar.MONTH, Calendar.JANUARY);
        deadline.set(Calendar.DAY_OF_MONTH, 20);
        deadline.set(Calendar.HOUR_OF_DAY, 0);
        deadline.set(Calendar.MINUTE, 0);
        deadline.set(Calendar.SECOND, 0);
        deadline.set(Calendar.MILLISECOND, 0);

        Student student = (Student) sc.createUser("cameronrolfe", "password");

        ArrayList<String> prerequisites = new ArrayList<>();

        CourseData courseData = cc.createCourse("COMP3004A", "Object-Oriented Software Engineering", 100, days, "12:30", 1.5, prerequisites);

        assertNotNull(courseData);

        Professor professor = (Professor) pc.createUser("JeanPierreCorriveau", "password");

        courseData.attach(professor);


        //Get registration status
        int studentRegistrationStatus = student.canStudentRegisterInCourse(courseData, date, deadline);

        //Assert registration status is 0 (Student can register)
        assertEquals(studentRegistrationStatus, 0);

        //Assert that before attaching student to course they do not have course
        assertEquals(student.getCourses().size(),0);

        assertEquals(courseData.getCurrStudents(), 0);

        assertNull(student.getCourses().get(courseData.getCourseCode()));

        //Attach Student to Course
        courseData.attach(student);

        //Verify that student was added to course

        assertEquals(student.getCourses().size(), 1);

        assertNotNull(student.getCourses().get(courseData.getCourseCode()));

        assertEquals(courseData.getCurrStudents(), 1);


    }

    //Test to verify that student cannot register past deadline
    @Test
    public void testCourseRegistrationPastDeadline() {
        ArrayList<String> days = new ArrayList<>();
        days.add("Tuesday");
        days.add("Thursday");

        Calendar date = Calendar.getInstance();
        Calendar deadline = Calendar.getInstance();

        //Setting date to January 21st 2021 at 0:00
        date.set(Calendar.MONTH, Calendar.JANUARY);
        date.set(Calendar.DAY_OF_MONTH, 21);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        //Setting last registration date to random date of January 20th
        deadline.set(Calendar.MONTH, Calendar.JANUARY);
        deadline.set(Calendar.DAY_OF_MONTH, 20);
        deadline.set(Calendar.HOUR_OF_DAY, 0);
        deadline.set(Calendar.MINUTE, 0);
        deadline.set(Calendar.SECOND, 0);
        deadline.set(Calendar.MILLISECOND, 0);

        Student student = (Student) sc.createUser("cameronrolfe", "password");

        ArrayList<String> prerequisites = new ArrayList<>();

        CourseData courseData = new CourseData("COMP3004A", "Object-Oriented Software Engineering", 100, days, "12:30", 1.5, prerequisites);

        Professor professor = (Professor) pc.createUser("JeanPierreCorriveau", "password");

        courseData.attach(professor);


        //Retrieve Registration Status
        int studentRegistrationStatus = student.canStudentRegisterInCourse(courseData, date, deadline);

        assertNotNull(courseData);

        //Assert that registration status is 1 (Course Registration has Past)
        assertEquals(studentRegistrationStatus, 1);
    }

    //Test to verify that student cannot register in course when it is full
    @Test
    public void testCourseRegistrationCourseFull() {
        ArrayList<String> days = new ArrayList<>();
        days.add("Tuesday");
        days.add("Thursday");

        Calendar date = Calendar.getInstance();
        Calendar deadline = Calendar.getInstance();

        //Setting date to January 1st 2021 at 0:00
        date.set(Calendar.MONTH, Calendar.JANUARY);
        date.set(Calendar.DAY_OF_MONTH, 1);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        //Setting last registration date to random date of January 20th
        deadline.set(Calendar.MONTH, Calendar.JANUARY);
        deadline.set(Calendar.DAY_OF_MONTH, 20);
        deadline.set(Calendar.HOUR_OF_DAY, 0);
        deadline.set(Calendar.MINUTE, 0);
        deadline.set(Calendar.SECOND, 0);
        deadline.set(Calendar.MILLISECOND, 0);

        //Creating 4 different students
        Student student = (Student) sc.createUser("cameronrolfe", "password");
        Student student2 = (Student) sc.createUser("benwilliams", "password");
        Student student3 = (Student) sc.createUser("jaxsonhood", "password");
        Student student4 = (Student) sc.createUser("camerondickie", "password");

        ArrayList<String> prerequisites = new ArrayList<>();

        //Creating course with maximum amount of students 3
        int maxStudents = 3;

        CourseData courseData = cc.createCourse("COMP3004A", "Object-Oriented Software Engineering", maxStudents, days, "12:30", 1.5, prerequisites);

        System.out.println(courseData.getCurrStudents());

        assertNotNull(courseData);

        Professor professor = (Professor) pc.createUser("JeanPierreCorriveau", "password");

        courseData.attach(professor);

        assertEquals(student.canStudentRegisterInCourse(courseData, date, deadline), 0);
        courseData.attach(student);
        assertEquals(student2.canStudentRegisterInCourse(courseData, date, deadline),0);
        courseData.attach(student2);
        System.out.println(student3.canStudentRegisterInCourse(courseData, date, deadline));
        assertEquals(student3.canStudentRegisterInCourse(courseData, date, deadline), 0);
        courseData.attach(student3);
        assertEquals(student4.canStudentRegisterInCourse(courseData, date, deadline), 2);
    }

    //Test to verify that student cannot register in course when they do not have the prerequisite courses
    @Test
    public void testCourseRegistrationStudentDoesNotHavePrerequisites() {
        ArrayList<String> days = new ArrayList<>();
        days.add("Tuesday");
        days.add("Thursday");

        Calendar date = Calendar.getInstance();
        Calendar deadline = Calendar.getInstance();

        //Setting date to January 1st 2021 at 0:00
        date.set(Calendar.MONTH, Calendar.JANUARY);
        date.set(Calendar.DAY_OF_MONTH, 1);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        //Setting last registration date to random date of January 20th
        deadline.set(Calendar.MONTH, Calendar.JANUARY);
        deadline.set(Calendar.DAY_OF_MONTH, 20);
        deadline.set(Calendar.HOUR_OF_DAY, 0);
        deadline.set(Calendar.MINUTE, 0);
        deadline.set(Calendar.SECOND, 0);
        deadline.set(Calendar.MILLISECOND, 0);

        //Creating Student with past course COMP2404
        Student student = (Student) sc.createUser("cameronrolfe", "password");

        student.addPastCourse("COMP2404");
        student.addPastCourse("COMP2804");

        //Creating list of prerequisites for course  COMP2404, COMP2402
        ArrayList<String> prerequisites = new ArrayList<>();
        prerequisites.add("COMP2404");
        prerequisites.add("COMP2402");

        CourseData courseData = cc.createCourse("COMP3004A", "Object-Oriented Software Engineering", 100, days, "12:30", 1.5, prerequisites);

        assertNotNull(courseData);

        Professor professor = (Professor) pc.createUser("johnsmith", "password");

        courseData.attach(professor);

        //Retrieve Registration Status from student.canStudentRegisterInCourse function
        int studentRegistrationStatus = student.canStudentRegisterInCourse(courseData, date, deadline);

        //Assert that studentRegistrationStatus is 3 (Student does not meet prerequisites)
        assertEquals(studentRegistrationStatus, 3);
    }

    //Test to verify that student can register in a course they have the prerequisites for
    @Test
    public void testCourseRegistrationStudentDoesHavePrerequisites() {
        ArrayList<String> days = new ArrayList<>();
        days.add("Tuesday");
        days.add("Thursday");

        Calendar date = Calendar.getInstance();
        Calendar deadline = Calendar.getInstance();

        //Setting date to January 1st 2021 at 0:00
        date.set(Calendar.MONTH, Calendar.JANUARY);
        date.set(Calendar.DAY_OF_MONTH, 1);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        //Setting last registration date to random date of January 20th
        deadline.set(Calendar.MONTH, Calendar.JANUARY);
        deadline.set(Calendar.DAY_OF_MONTH, 20);
        deadline.set(Calendar.HOUR_OF_DAY, 0);
        deadline.set(Calendar.MINUTE, 0);
        deadline.set(Calendar.SECOND, 0);
        deadline.set(Calendar.MILLISECOND, 0);

        //Creating student with past courses COMP2402, COMP2404, COMP2804
        Student student = (Student) sc.createUser("cameronrolfe", "password");
        student.addPastCourse("COMP2402");
        student.addPastCourse("COMP2404");
        student.addPastCourse("COMP2804");

        //Creating list of course prerequisites COMP2402, COMP2404
        ArrayList<String> prerequisites = new ArrayList<>();
        prerequisites.add("COMP2402");
        prerequisites.add("COMP2404");

        CourseData courseData = cc.createCourse("COMP3004A", "Object-Oriented Software Engineering", 100, days, "12:30", 1.5, prerequisites);

        assertNotNull(courseData);

        Professor professor = (Professor) pc.createUser("JeanPierreCorriveau", "password");

        courseData.attach(professor);

        //Retrieve registration status from student.canStudentRegisterInCourse function
        int studentRegistrationStatus = student.canStudentRegisterInCourse(courseData, date, deadline);

        //Assert that registration status is 0 (Student can register)
        assertEquals(studentRegistrationStatus, 0);
    }

    //Test to verify that student cannot register in a course that would cause timetable conflicts
    @Test
    public void testStudentCourseRegistrationTimetableConflict() {
        Calendar date = Calendar.getInstance();
        Calendar deadline = Calendar.getInstance();

        //Setting date to January 1st 2021 at 0:00
        date.set(Calendar.MONTH, Calendar.JANUARY);
        date.set(Calendar.DAY_OF_MONTH, 1);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        //Setting last registration date to random date of January 20th
        deadline.set(Calendar.MONTH, Calendar.JANUARY);
        deadline.set(Calendar.DAY_OF_MONTH, 20);
        deadline.set(Calendar.HOUR_OF_DAY, 0);
        deadline.set(Calendar.MINUTE, 0);
        deadline.set(Calendar.SECOND, 0);
        deadline.set(Calendar.MILLISECOND, 0);

        Student student = (Student) sc.createUser("cameronrolfe", "password");
        student.addPastCourse("COMP2804");

        ArrayList<String> prerequisites = new ArrayList<>();

        ArrayList<String> days = new ArrayList<>();
        days.add("Tuesday");
        days.add("Thursday");

        ArrayList<String> days2 = new ArrayList<>();
        days2.add("Monday");
        days2.add("Wednesday");

        CourseData courseData = cc.createCourse("COMP3004A", "Object-Oriented Software Engineering", 100, days, "12:30", 1.5, prerequisites);

        assertNotNull(courseData);

        Professor professor = (Professor) pc.createUser("JeanPierreCorriveau", "password");

        courseData.attach(professor);

        assertEquals(student.canStudentRegisterInCourse(courseData, date, deadline), 0);

        student.update("addCourse", courseData);
        courseData.attach(student);

        //Creating different courses that student could potentially register in
        CourseData courseData2 = cc.createCourse("COMP3203A", "Networking", 100, days, "13:00", 3, prerequisites);

        CourseData courseData3 = cc.createCourse("COMP3005A", "Databases", 100, days, "11:30", 1.5, prerequisites);

        CourseData courseData4 = cc.createCourse("COMP3203A", "Networking", 100, days, "16:00", 3, prerequisites);

        CourseData courseData5 = cc.createCourse("COMP3005B", "Databases", 100, days, "9:00", 1.5, prerequisites);

        CourseData courseData6 = cc.createCourse("COMP3004B", "Object Oriented Software Engineering", 100, days2, "15:00", 2, prerequisites);


        //Asserting that course 2 and course 3 registration status for course 2 and 3 is 4 (Timetable conflicts)
        assertEquals(student.canStudentRegisterInCourse(courseData2, date, deadline), 4);
        assertEquals(student.canStudentRegisterInCourse(courseData3, date, deadline), 4);

        //Asserting that registration status for courses 3-6 is 0 (Student can register)
        assertEquals(student.canStudentRegisterInCourse(courseData4, date, deadline), 0);
        assertEquals(student.canStudentRegisterInCourse(courseData4, date, deadline),0);
        assertEquals(student.canStudentRegisterInCourse(courseData5, date, deadline), 0);
        assertEquals(student.canStudentRegisterInCourse(courseData6, date, deadline), 0);
    }

    //Test to verify that student can withdraw from a course when it is before the deadline
    //Also verifies the following:
    //- CourseData.attach(student) increases total number of students in course (CourseData currStudents attribute)
    //- CourseData.detach(student) decreases total number of students in course (CourseData currStudents attribute)
    //- CourseData.attach(student) adds CourseData to students courses
    //- CourseData.attach(student) deletes CourseData from students courses

    @Test
    public void testCourseWithdrawal() {
        ArrayList<String> days = new ArrayList<>();
        days.add("Tuesday");
        days.add("Thursday");

        Calendar date = Calendar.getInstance();
        Calendar deadline = Calendar.getInstance();

        //Setting date to January 1st 2021 at 0:00
        date.set(Calendar.MONTH, Calendar.JANUARY);
        date.set(Calendar.DAY_OF_MONTH, 1);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        //Setting last registration date to random date of January 20th
        deadline.set(Calendar.MONTH, Calendar.JANUARY);
        deadline.set(Calendar.DAY_OF_MONTH, 20);
        deadline.set(Calendar.HOUR_OF_DAY, 0);
        deadline.set(Calendar.MINUTE, 0);
        deadline.set(Calendar.SECOND, 0);
        deadline.set(Calendar.MILLISECOND, 0);

        Student student = (Student) sc.createUser("cameronrolfe", "password");
        student.addPastCourse("COMP2804");

        ArrayList<String> prerequisites = new ArrayList<>();

        CourseData courseData = cc.createCourse("COMP3004A", "Object-Oriented Software Engineering", 100, days, "12:30", 1.5, prerequisites);

        assertNotNull(courseData);

        Professor professor = (Professor) pc.createUser("JeanPierreCorriveau", "password");

        courseData.attach(professor);

        assertEquals(student.canStudentRegisterInCourse(courseData, date, deadline), 0);

        assertNull(student.getCourses().get(courseData.getCourseCode())); //Verifying student does not already have course

        //Attach Student to Course
        courseData.attach(student);

        assertEquals(student.getCourses().size(), 1);   //Verifying number of student courses increased

        assertEquals(courseData.getCurrStudents(), 1);  //Verifying number of students in course increased

        assertNotNull(student.getCourses().get(courseData.getCourseCode())); //Student has course

        assertTrue(student.canStudentWithdraw(date, deadline)); //Verifying that student is able to withdraw in course

        courseData.detach(student); //Detaching student from course


        assertEquals(courseData.getCurrStudents(), 0);  //Verifying number of students in course went down

        assertNull(student.getCourses().get(courseData.getCourseCode()));  //Verifying that student no longer has course
    }

    //Test to verify that student cannot withdraw from a course when it is past the deadline
    @Test
    public void testCourseWithdrawalPastDeadline() {
        ArrayList<String> days = new ArrayList<>();
        days.add("Tuesday");
        days.add("Thursday");

        Calendar date = Calendar.getInstance();
        Calendar deadline = Calendar.getInstance();

        //Setting date to January 1st 2021 (Before Deadline)
        date.set(Calendar.MONTH, Calendar.JANUARY);
        date.set(Calendar.DAY_OF_MONTH, 1);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        //Setting last registration date to random date of January 20th
        deadline.set(Calendar.MONTH, Calendar.JANUARY);
        deadline.set(Calendar.DAY_OF_MONTH, 20);
        deadline.set(Calendar.HOUR_OF_DAY, 0);
        deadline.set(Calendar.MINUTE, 0);
        deadline.set(Calendar.SECOND, 0);
        deadline.set(Calendar.MILLISECOND, 0);

        Student student = (Student) sc.createUser("cameronrolfe", "password");
        student.addPastCourse("COMP2804");

        ArrayList<String> prerequisites = new ArrayList<>();

        CourseData courseData = cc.createCourse("COMP3004A", "Object-Oriented Software Engineering", 100, days, "12:30", 1.5, prerequisites);

        assertNotNull(courseData);

        Professor professor = (Professor) pc.createUser("JeanPierreCorriveau", "password");

        courseData.attach(professor);

        assertEquals(student.canStudentRegisterInCourse(courseData, date, deadline), 0);

        //Adding Student to Course
        student.update("addCourse", courseData);
        courseData.attach(student);

        assertEquals(student.getCourses().size(),1);

        assertEquals(courseData.getCurrStudents(), 1);

        //Setting Date to the 20th of the current month (January) so after the deadline
        date.set(Calendar.DAY_OF_MONTH, 20);

        //Asserting that student cannot withdraw as it is past deadline
        assertTrue(!student.canStudentWithdraw(date, deadline));
    }

    //Test to verify that a Professor cannot be assigned to a course that would cause timetable conflicts
    @Test
    public void testCourseProfessorTimetableConflict() {
        Calendar date = Calendar.getInstance();
        Calendar deadline = Calendar.getInstance();

        //Setting date to January 1st 2021 at 0:00
        date.set(Calendar.MONTH, Calendar.JANUARY);
        date.set(Calendar.DAY_OF_MONTH, 1);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        //Setting last registration date to random date of January 20th
        deadline.set(Calendar.MONTH, Calendar.JANUARY);
        deadline.set(Calendar.DAY_OF_MONTH, 20);
        deadline.set(Calendar.HOUR_OF_DAY, 0);
        deadline.set(Calendar.MINUTE, 0);
        deadline.set(Calendar.SECOND, 0);
        deadline.set(Calendar.MILLISECOND, 0);

        ArrayList<String> prerequisites = new ArrayList<>();
        prerequisites.add("COMP2804");

        ArrayList<String> days = new ArrayList<>();
        days.add("Tuesday");
        days.add("Thursday");

        ArrayList<String> days2 = new ArrayList<>();
        days2.add("Monday");
        days2.add("Wednesday");

        CourseData courseData = cc.createCourse("COMP3004A", "Object-Oriented Software Engineering", 100, days, "12:30", 1.5, prerequisites);

        Professor professor = (Professor) pc.createUser("JeanPierreCorriveau", "password");

        assertTrue(professor.canProfessorBeAssignedToCourse(courseData));

        assertNotNull(courseData);

        courseData.attach(professor);

        //Creating different courses a professor could be assigned too
        CourseData courseData2 = cc.createCourse("COMP3203A", "Networking", 100, days, "13:00", 3, prerequisites);

        CourseData courseData3 = cc.createCourse("COMP3005A", "Databases", 100, days, "11:30", 1.5, prerequisites);

        CourseData courseData4 = cc.createCourse("COMP3203A", "Networking", 100, days, "16:00", 3, prerequisites);

        CourseData courseData5 = cc.createCourse("COMP3005B", "Databases", 100, days, "9:00", 1.5, prerequisites);

        CourseData courseData6 = cc.createCourse("COMP3004A", "Object Oriented Software Engineering", 100, days2, "9:00", 2, prerequisites);

        CourseData courseData7 = cc.createCourse("COMP3004B", "Object Oriented Software Engineering", 100, days2, "15:00", 2, prerequisites);

        //Asserting that the professor cannot be assigned to course 2 or 3 as there is a timetable conflict
        assertFalse(professor.canProfessorBeAssignedToCourse(courseData2));

        assertFalse(professor.canProfessorBeAssignedToCourse(courseData3));

        //Asserting that the professor can be assigned to course 4 - 7
        assertTrue(professor.canProfessorBeAssignedToCourse(courseData4));

        assertTrue(professor.canProfessorBeAssignedToCourse(courseData5));

        assertTrue(professor.canProfessorBeAssignedToCourse(courseData6));

        assertTrue(professor.canProfessorBeAssignedToCourse(courseData7));
    }


}