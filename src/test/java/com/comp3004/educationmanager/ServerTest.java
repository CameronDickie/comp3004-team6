package com.comp3004.educationmanager;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Server.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerTest {
    @Test
    @Order(1)
    public void testServerInitialization() {

    }
}
