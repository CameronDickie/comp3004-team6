package com.comp3004.educationmanager.factory;
import com.comp3004.educationmanager.accounts.User;
import com.comp3004.educationmanager.accounts.Student;


public class StudentCreator implements UserCreator{

    public User createUser(int userID, String username, String password) {

        Student student = new Student(userID, username, password);

        return student;
    }
}
