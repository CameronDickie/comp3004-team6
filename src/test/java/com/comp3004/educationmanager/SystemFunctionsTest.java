package com.comp3004.educationmanager;

import com.comp3004.educationmanager.accounts.Admin;
import com.comp3004.educationmanager.accounts.Professor;
import com.comp3004.educationmanager.accounts.Student;
import com.comp3004.educationmanager.accounts.User;
import com.comp3004.educationmanager.composite.Component;
import com.comp3004.educationmanager.decorator.FileDecorator;
import com.comp3004.educationmanager.factory.AdminCreator;
import com.comp3004.educationmanager.factory.CourseCreator;
import com.comp3004.educationmanager.factory.ProfessorCreator;
import com.comp3004.educationmanager.factory.StudentCreator;
import com.comp3004.educationmanager.observer.CourseData;
import com.comp3004.educationmanager.observer.SystemData;
import com.comp3004.educationmanager.strategy.AddDocumentStrategy;
import com.comp3004.educationmanager.strategy.CourseContentStrategy;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

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

    @Test
    @Order(1)
    public void testSystemCreation() {
        system = new SystemData();

        User admin = ac.createUser("admin", "pass");
        assertNotNull(admin);

        system.attach(admin);
        assertTrue(system.getObservers().size() > 0);
    }

    @Test
    @Order(2)
    public void testProfessorCreation() {
        professor = (Professor) pc.createUser("prof", "pass");
        assertNotNull(professor);
    }

    @Test
    @Order(3)
    public void testCourseCreation() {
        course = cc.createCourse("COMP3004B", "Design Patterns", 150);
        assertNotNull(course);

        course.attach(professor);

        assertTrue(course.getObservers().size() > 0);
        assertTrue(professor.getCourses().size() > 0);
    }

    @Test
    @Order(4)
    public void testStudentCreation() {
        student = (Student) sc.createUser("student", "pass");
        assertNotNull(student);
    }

    @Test
    @Order(5)
    public void testCourseRegistration() {
        course.attach(student);

        assertTrue(course.getObservers().size() > 1);
        assertTrue(student.getCourses().size() > 0);
    }

    @Test
    @Order(6)
    public void testAddingContent() {
//        course.setStrategy(new CourseContentStrategy());
//        Component c = course.addContent("testContent", "/COMP3004B/", "section");
//
//        course.setStrategy(new AddDocumentStrategy());
//        c = course.addContent("testDoc", "/COMP3004B/testContent/", "PDF");
//        c.setProperty("file", new byte[] {1, 2, 3, 4});
//        assertNotNull(c.executeCommand("download", null));
//
//        Component test = (Component) course.getContent().executeCommand("findByPath", "/COMP3004B/testContent/testDoc/");
//        assertNotNull(test);
//
//        System.out.println((String) test.executeCommand("stringify", null));
//
//        String content = (String) course.getContent().executeCommand("stringify", null);
//        System.out.println(content);
//
//        byte[] downloadBytes = (byte[]) test.executeCommand("download", null);
//        byte[] viewBytes = (byte[]) test.executeCommand("viewAsPDF", null);
//
//        assertNotNull(downloadBytes);
//        assertNotNull(viewBytes);
    }
}