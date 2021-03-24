package com.comp3004.educationmanager.db.repositories;

import javax.persistence.*;

@Entity
@Table(name = "userdetails")
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column
    private String type;
    public UserDetails() {
        name = null;
        password = null;
        type = "";
    }

    public UserDetails(String s, String p, String t){
        name = s;
        password = p;
        type = t;
    }

    public String getName(){
        return name;
    }

    public long getId() {
        return this.id;
    }
    public String getPassword() {
        return this.password;
    }
}
