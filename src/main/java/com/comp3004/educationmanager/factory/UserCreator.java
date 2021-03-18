package com.comp3004.educationmanager.factory;

import com.comp3004.educationmanager.accounts.User;

public interface UserCreator {
    public User createUser(int userID, String username, String password);
}
