package com.comp3004.educationmanager;


import com.comp3004.educationmanager.accounts.User;
import com.comp3004.educationmanager.db.H2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServerState {
    @Autowired
    H2 db;
    public ServerState () {
        System.out.println("Server state is being made...");
    }
    public boolean createUser(User u) {
        return db.addUser(u);
    }
    public void print() {
        db.print();
    }
    public boolean auth(String uname, String pword) {
        return db.auth(uname, pword);
    }
}
