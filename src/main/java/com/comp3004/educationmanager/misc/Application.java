package com.comp3004.educationmanager.misc;

public class Application {
    protected String firstname;
    protected String lastname;
    protected String type;
    private String password;
    public Application(String fname, String lname, String pword, String type) {
        this.firstname = fname;
        this.lastname = lname;
        this.password = pword;
        this.type = type;
    }
    public String getFirstname() {
        return this.firstname;
    }
    public String getLastname() {
        return this.lastname;
    }
    public String getType() {
        return this.type;
    }
    public String getPassword() {
        return this.password;
    }
}
