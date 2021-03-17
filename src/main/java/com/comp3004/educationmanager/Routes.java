package com.comp3004.educationmanager;
import com.comp3004.educationmanager.accounts.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.MediaType;

@RestController
public class Routes {
    // For things like converting Objects to json
    Helper help = new Helper();

    @GetMapping("/api/members")
    public String members() {
        return "Cameron, Cameron, Ben, Jaxson";
    }

    @PostMapping(value = "/api/login", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String login(@RequestBody String username) {
        System.out.println("From '/api/login': " + username);
        Student a = new Student(1, username, "1234");
        return help.objectToJSONString(a);
    }
}



