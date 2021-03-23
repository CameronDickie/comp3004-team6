package com.comp3004.educationmanager.db;

import com.comp3004.educationmanager.accounts.User;

public interface Database {
    void initialize();
    boolean addUser(User user);
    void print();
}
