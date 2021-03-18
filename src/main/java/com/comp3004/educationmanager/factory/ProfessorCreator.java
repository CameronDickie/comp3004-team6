package com.comp3004.educationmanager.factory;

import com.comp3004.educationmanager.accounts.User;
import com.comp3004.educationmanager.accounts.Professor;

public class ProfessorCreator implements UserCreator {

    public User createUser(int userID, String username, String password) {

        Professor professor = new Professor(userID, username, password);

        return professor;
    }
}
