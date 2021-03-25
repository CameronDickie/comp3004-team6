package com.comp3004.educationmanager.factory;
import com.comp3004.educationmanager.accounts.Admin;
import com.comp3004.educationmanager.accounts.Student;
import com.comp3004.educationmanager.accounts.User;

public class AdminCreator {
    public User createUser(String username, String password) {
        //without parameters, username and password default to 'admin' and 'pass' respectively.
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(password);
        return admin;
    }
}
