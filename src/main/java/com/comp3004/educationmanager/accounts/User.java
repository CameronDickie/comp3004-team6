package com.comp3004.educationmanager.accounts;

import javax.persistence.*;

@MappedSuperclass
public abstract class User {

    @Id
    protected long id;

    @Column(name = "username")
    protected String username;

    @Column(name = "password")
    protected String password;

    public String getName() {
        return this.username;
    }
    public long getUserId() {
        return this.id;
    }
    public String getPassword() {
        return this.password;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}