package com.comp3004.educationmanager.factory;

import com.comp3004.educationmanager.accounts.User;
import com.comp3004.educationmanager.accounts.Professor;

public class ProfessorCreator implements UserCreator {

    public User createUser(String username, String password) {
        return new Professor(username, password);
    }
}
