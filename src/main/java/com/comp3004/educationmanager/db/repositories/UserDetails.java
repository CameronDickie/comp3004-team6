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

    @Column(name = "message")
    private String message;

    public UserDetails(String s, String msg){
        name = s;
        message = msg;
    }

    public String getName(){
        return name;
    }
}
