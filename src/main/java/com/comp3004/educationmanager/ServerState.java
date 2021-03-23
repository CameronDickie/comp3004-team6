package com.comp3004.educationmanager;


import com.comp3004.educationmanager.accounts.User;
import com.comp3004.educationmanager.db.Database;
import com.comp3004.educationmanager.db.H2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServerState {
    @Autowired
    H2 db;
    public ServerState () {
        System.out.println("Server state is being made...");
//        db = new H2();
    }
    public boolean createUser(User u) {
        db.addUser(u);
        return true;
    }
    public void printUsers() {
        db.print();
    }
}
