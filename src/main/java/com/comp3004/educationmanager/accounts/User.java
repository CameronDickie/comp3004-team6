package com.comp3004.educationmanager.accounts;

import com.comp3004.educationmanager.observer.Observer;

import javax.persistence.*;

/*
The base class for all Users
Extended by Admin, Student, and Professor
 */
@MappedSuperclass
public abstract class User implements Observer, java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @Column(name = "username")
    protected String username;

    @Column(name = "password")
    protected String password;

    /*
    Getters
     */
    public String getName() {
        return this.username;
    }
    public long getUserId() {
        return this.id;
    }
    public String getPassword() {
        return this.password;
    }

    /*
    Setters
     */
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}