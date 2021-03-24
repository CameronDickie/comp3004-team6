package com.comp3004.educationmanager.db;

import com.comp3004.educationmanager.accounts.Admin;
import com.comp3004.educationmanager.accounts.Professor;
import com.comp3004.educationmanager.accounts.Student;
import com.comp3004.educationmanager.accounts.User;
import com.comp3004.educationmanager.db.repositories.UserDetails;
import com.comp3004.educationmanager.db.repositories.UserRepository;
import com.comp3004.educationmanager.factory.AdminCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class H2 implements Database {

    @Autowired
    UserRepository ur;

    public H2() {

    }
    @PostConstruct
    @Override
    public void initialize() {
        //create the admin user and add them to the database
        AdminCreator factory = new AdminCreator();
        User admin = factory.createUser("admin", "pass");
        if (addUser(admin)) {
            System.out.println("admin user added to the db");
        } else {
            System.out.println("admin has failed to be created");
        }
    }

    @Override
    public boolean addUser(User user) {
        //insert this user based on its type
        String type = "";
        if(user.getClass() == Student.class) type = "student";
        else if(user.getClass() == Professor.class) type = "professor";
        else if(user.getClass() == Admin.class) type = "admin";
        else type = "null";
        UserDetails entry = ur.save(new UserDetails(user.getName(), user.getPassword(), type));
        return true;
    }

    @Override
    public boolean auth(String username, String password) {
        List<UserDetails> entries = ur.findAll();
        for(UserDetails entry : entries) {
            if(entry.getName().equals(username) && entry.getPassword().equals(password)) return true;
        }
        return false;
    }
    @Override
    public void print() {
        List<UserDetails> entries = ur.findAll();
        for(UserDetails entry : entries)
            System.out.println(entry.getId() + " | " + entry.getName() + " | " + entry.getPassword());
    }
}
