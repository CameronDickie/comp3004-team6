package com.comp3004.educationmanager.websocket;

import com.comp3004.educationmanager.Helper;
import com.comp3004.educationmanager.ServerState;
import com.comp3004.educationmanager.accounts.User;
import com.comp3004.educationmanager.observer.SystemData;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.SubProtocolCapable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerWebSocketHandler extends TextWebSocketHandler implements SubProtocolCapable {
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(ServerWebSocketHandler.class);
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("Server connection opened");
        sessions.add(session);
        TextMessage message = new TextMessage("one-time message from server");
        logger.info("Server sends: {}", message);

        session.sendMessage(message);
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        logger.info("Server connection closed: {}", status);
        sessions.remove(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String request = message.getPayload();
        logger.info("Server received: {}", request);
        HashMap<String, Object> typeMap = Helper.stringToMap(request);
        /*
        Code for attaching a web socket to a given user
            TODO:
                ensure that the message sent is stating that this is meant for the courses or notifications, and can handle admin user
         */
        //this is the path for when a user logs in and must attach its WebSocketSession to its User object.
        if(typeMap.get("attachUser") != null) {
            //search for the user with the id in message
            HashMap<String, Object> map = (HashMap<String, Object>) typeMap.get("attachUser");
            HashMap<Long, User> users = SystemData.users;
            //determine if this is a professor or student
            Object id = map.get("studentID");
            if(id == null) {
                id = map.get("professorID");
            }
            if(id == null && map.get("name").equals("admin")) {
                //this is likely the admin, or we have broken the code
                SystemData.admin.setSocketConnection(session);
                String response = String.format("response from server to '%s'", request);
                logger.info("Server sends: {}", response);
                session.sendMessage(new TextMessage(response));
                return;
            }
            if(id == null) {
                System.out.println("Id error getting user @ Web Socket");
                return;
            }
            User u = users.get(Long.valueOf((Integer) id)); //get the user with this user id
            if(u == null) {
                System.out.println("Failed to find the user with this id");
                return;
            }
            u.setSocketConnection(session); //give this user their session instance
            if(u.getSocketConnection() == null) {
                System.out.println("failed to add the socket connection");
            }

            //attach session to that user
            String response = String.format("response from server to '%s'", request);
            logger.info("Server sends: {}", response);
            session.sendMessage(new TextMessage(response));
        }

    }

//    @Scheduled(fixedRate = 10000)
//    void sendPeriodicMessages() throws IOException {
//        for (WebSocketSession session : sessions) {
//            if(session.isOpen()) {
//                String broadcast = "server periodic message " + LocalTime.now();
//                logger.info("Server sends: {}", broadcast);
//                session.sendMessage(new TextMessage(broadcast));
//            }
//        }
//    }
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        logger.info("Server transport error: {}", exception.getMessage());
    }
    public List<String> getSubProtocols() {
        return Collections.singletonList("subprotocol.demo.websocket");

    }
}
