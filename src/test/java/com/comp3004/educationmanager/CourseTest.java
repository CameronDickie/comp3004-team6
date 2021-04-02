package com.comp3004.educationmanager;

import com.comp3004.educationmanager.accounts.Professor;
import com.comp3004.educationmanager.composite.Component;
import com.comp3004.educationmanager.factory.ProfessorCreator;
import com.comp3004.educationmanager.factory.StudentCreator;
import com.comp3004.educationmanager.observer.CourseData;
import com.comp3004.educationmanager.strategy.AddDocumentStrategy;
import com.comp3004.educationmanager.strategy.CourseContentStrategy;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertEquals;

@SpringBootTest(classes = Server.class)
public class CourseTest {
    CourseData course = new CourseData("COMP3004B", "Design Patterns", 150);
    ProfessorCreator pc = new ProfessorCreator();
    StudentCreator sc = new StudentCreator();

    @Test
    public void testCourseRegistration() {
        course.attach(sc.createUser("testUser", "password"));

    }

    @Test
    public void testAddingContent() {
        course.setStrategy(new CourseContentStrategy());
        Component c = course.addContent("testContent", "/COMP3004B/");
        course.setStrategy(new AddDocumentStrategy());
        c = course.addContent("testDoc", "/COMP3004B/testContent/");

        assertNotNull(c);

        String path = (String) ((Component) course.getContent().executeCommand("findByPath", "/COMP3004B/testContent/testDoc/")).getProperty("fullPath");
        assertEquals((String) c.getProperty("fullPath"), path);
    }
}
