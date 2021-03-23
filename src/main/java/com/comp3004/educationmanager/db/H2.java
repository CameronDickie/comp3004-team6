package com.comp3004.educationmanager.db;

import com.comp3004.educationmanager.accounts.User;
import com.comp3004.educationmanager.db.repositories.UserDetails;
import com.comp3004.educationmanager.db.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

@Component
public class H2 implements Database {

    @Autowired
    UserRepository ur;

    public H2() {

    }

    @Override
    public void initialize() {
        //set up the userrepository
    }

    @Override
    public boolean addUser(User user) {
        //insert this user based on its type
        UserDetails entry = ur.save(new UserDetails("Cameron D.", "Is the best Cameron"));
        return true;
    }

    @Override
    public void print() {
        List<UserDetails> entries = ur.findAll();
        for(UserDetails entry : entries) {
            System.out.println(entry.getName());
        }
    }
}
