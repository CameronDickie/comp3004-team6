package com.comp3004.educationmanager.accounts;

import com.comp3004.educationmanager.observer.Observer;
import org.springframework.web.socket.WebSocketSession;

import javax.persistence.*;

/*
The base class for all Users
Extended by Admin, Student, and Professor
 */
@MappedSuperclass
public abstract class User implements Observer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @Transient
    WebSocketSession socketConnection;

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
    public WebSocketSession getSocketConnection() { return this.socketConnection; }
    /*
    Setters
     */
    public void setSocketConnection(WebSocketSession s) {this.socketConnection = s;}
//    public void setSocketConnection(MyTextWebSocketHandler s) { this.socketConnection = s;}
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}