package com.comp3004.educationmanager.factory;
import com.comp3004.educationmanager.accounts.User;
import com.comp3004.educationmanager.accounts.Student;


public class StudentCreator implements UserCreator{

    public User createUser(String username, String password) {
        return new Student(username, password);
    }
}
