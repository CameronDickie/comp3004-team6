package com.comp3004.educationmanager.factory;
import com.comp3004.educationmanager.accounts.User;
import com.comp3004.educationmanager.accounts.Student;


public class StudentCreator implements UserCreator{

    public User createUser(String username, String password) {
        Student s = new Student();
        s.setUsername(username);
        s.setPassword(password);
        return s;
    }
}
