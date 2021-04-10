package com.comp3004.educationmanager.db;

import com.comp3004.educationmanager.accounts.User;
import com.comp3004.educationmanager.observer.CourseData;

public interface Database {
    boolean addUser(User user);
    boolean auth(String username, String password);
    void print();

}
