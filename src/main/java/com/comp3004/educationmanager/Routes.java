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
    public String login(@RequestBody String userinfo) {
        System.out.println("From '/api/login': " + userinfo);
        HashMap<String, String> map = help.stringToMap(userinfo);
        //using this userinfo, see if there is a user with this information (auth)
        String answer = "";
        System.out.println(map.get("username"));
        System.out.println(map.get("password"));
        try {
            if (s.auth(map.get("username"), map.get("password"))) {
                answer = "true";
            } else {
                answer = "false";
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        s.printUsers();
        return answer;
    }
}



