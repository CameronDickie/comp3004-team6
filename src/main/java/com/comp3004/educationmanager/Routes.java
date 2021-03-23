package com.comp3004.educationmanager;
import com.comp3004.educationmanager.accounts.Student;
//import com.comp3004.educationmanager.db.repositories.UserRepository;
import com.comp3004.educationmanager.misc.UserNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.MediaType;

import java.util.HashMap;

@RestController
public class Routes {
    // For things like converting Objects to json
    Helper help = new Helper();
    @Autowired
    ServerState s;


    @GetMapping("/api/members")
    public String members() {
        return "Cameron, Cameron, Ben, Jaxson";
    }

    @PostMapping(value ="/api/register", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String register(@RequestBody String info) {
        System.out.println("From '/api/register': " + info);
        HashMap<String, String> map = help.stringToMap(info);
        //this is the notification to be added to the admin's list of notifications -- likely to be a part of the database, but for now I just want to get it all working
        UserNotification notification = new UserNotification(map.get("firstname"), map.get("lastname"), map.get("password"));
        return info + " has attempted to be registered";
    }

    @PostMapping(value = "/api/login", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String login(@RequestBody String username) {
        System.out.println("From '/api/login': " + username);
        Student a = new Student(username, "1234");
        try {
            s.createUser(a);
            s.printUsers();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        //TEST DATABASE
//        try {
//            UserDetails _userDetails = ur.save(new UserDetails("Jaxson", "YOU SUCK"));
//            System.out.println("Worked");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return help.objectToJSONString(a);
    }
}



