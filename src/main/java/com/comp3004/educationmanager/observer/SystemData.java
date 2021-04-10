package com.comp3004.educationmanager.observer;

import com.comp3004.educationmanager.accounts.*;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SystemData extends Subject {

    public static Admin admin;
    public static HashMap<Long, User> users = new HashMap<>();
    public static HashMap<String, CourseData> courses = new HashMap<>();

    @Override
    public boolean attach(Observer o) {
        return observers.add(o);
    }

    @Override
    public boolean detach(Observer o) {
        return observers.remove(o);
    }


    public void updateAll(String command, Object value) {
        if(command.equals("application")) {
            //send the message via web socket to the front-end
            TextMessage message = new TextMessage("get-applications");
            try {
                if(admin == null || admin.getSocketConnection() == null || !admin.getSocketConnection().isOpen()) {
                    System.out.println("Unable to connect to admin");
                    return;
                }
                admin.getSocketConnection().sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace(System.out);
            }
        } else if(command.equals("get-professor")) {
            TextMessage message = new TextMessage("get-professor");
            try {
                if(admin == null || admin.getSocketConnection() == null || !admin.getSocketConnection().isOpen()) {
                    System.out.println("Unable to connect to admin");
                    return;
                }
                admin.getSocketConnection().sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace(System.out);
            }
        } else if(command.equals("get-courses")) {
            TextMessage message = new TextMessage("get-courses");
            try {
                if(admin == null || admin.getSocketConnection() == null || !admin.getSocketConnection().isOpen()) {
                    System.out.println("Unable to connect to admin");
                    return;
                }
                admin.getSocketConnection().sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace(System.out);
            }
        }
    }
}
