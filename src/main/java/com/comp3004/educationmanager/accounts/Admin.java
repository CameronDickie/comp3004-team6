package com.comp3004.educationmanager.accounts;

import com.comp3004.educationmanager.misc.Application;
import org.springframework.web.socket.TextMessage;

import javax.persistence.Entity;
import java.io.IOException;
import java.util.ArrayList;

/*
Class for the Admin user
Implements all admin-specific functionality, including the observer
 */
@Entity
public class Admin extends User {
    ArrayList<Application> applications = new ArrayList<>();
    @Override
    public void update(String command, Object value) {
        if(command.equals("get-courses")) {
            TextMessage message = new TextMessage("get-courses");
            try {
                if(this.getSocketConnection() == null || !this.getSocketConnection().isOpen()) {
                    System.out.println("Unable to connect to the admin");
                    return;
                }
                this.getSocketConnection().sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace(System.out);
            }
        }
    }
    public void addApplication(Application a) {
        //check if this user has already applied
        for(Application app : applications) {
            if(app.getFirstname().equals(a.getFirstname()) && app.getLastname().equals(a.getLastname())) {
                return;
            }
        }
        //if they have not applied, add them to the list of applications
        applications.add(a);
    }
    public ArrayList<Application> getApplications() {
        return this.applications;
    }
}
