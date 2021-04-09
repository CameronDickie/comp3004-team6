package com.comp3004.educationmanager.accounts;

import com.comp3004.educationmanager.misc.Application;

import javax.persistence.Entity;
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
