package com.comp3004.educationmanager.factory;

import com.comp3004.educationmanager.accounts.User;
import com.comp3004.educationmanager.accounts.Professor;

public class ProfessorCreator implements UserCreator {

    static long professorID = 2000000;

    public User createUser(String username, String password) {
        Professor p = new Professor();
        p.setUsername(username);
        p.setPassword(password);
        p.setProfessorID(professorID);
        professorID++;
        return p;
    }
}
