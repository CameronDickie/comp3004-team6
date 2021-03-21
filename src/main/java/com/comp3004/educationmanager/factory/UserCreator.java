package com.comp3004.educationmanager.factory;

import com.comp3004.educationmanager.accounts.User;

public interface UserCreator {
    User createUser(String username, String password);
    
}
