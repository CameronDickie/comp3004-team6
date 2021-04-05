package com.comp3004.educationmanager;

import com.comp3004.educationmanager.accounts.User;
import com.comp3004.educationmanager.composite.Component;
import com.comp3004.educationmanager.factory.ProfessorCreator;
import com.comp3004.educationmanager.factory.StudentCreator;
import com.comp3004.educationmanager.observer.CourseData;
import com.comp3004.educationmanager.strategy.AddDocumentStrategy;
import com.comp3004.educationmanager.strategy.CourseContentStrategy;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Server.class)
public class CourseTest {
    CourseData course = new CourseData("COMP3004B", "Design Patterns", 150);
    ProfessorCreator pc = new ProfessorCreator();
    StudentCreator sc = new StudentCreator();

    @Test
    @Order(1)
    public void testProfessorCreation() {
        User user = sc.createUser("testUser", "password");
        assertNotNull(user);
    }

    @Test
    @Order(2)
    public void testCourseCreation() {

    }

    @Test
    @Order(3)
    public void testStudentCreation() {

    }

    @Test
    @Order(4)
    public void testCourseRegistration() {

    }

    @Test
    @Order(5)
    public void testAddingContent() {
        course.setStrategy(new CourseContentStrategy());
        Component c = course.addContent("testContent", "/COMP3004B/", "section");
        course.setStrategy(new AddDocumentStrategy());
        c = course.addContent("testDoc", "/COMP3004B/testContent/", "PPTX");

        assertNotNull(c);

        System.out.println((String) course.getContent().executeCommand("stringify", null));

        String path = (String) ((Component) course.getContent().executeCommand("findByPath", "/COMP3004B/testContent/testDoc/")).getProperty("fullPath");
        assertEquals((String) c.getProperty("fullPath"), path);
    }

    @Test
    @Order(6)
    public void testDownloadingContent() {

    }



    @Test
    @Order(5)
    public void testObserverUpdate() {

    }
}