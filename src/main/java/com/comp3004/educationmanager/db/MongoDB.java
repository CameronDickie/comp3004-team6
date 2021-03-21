package com.comp3004.educationmanager.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class MongoDB implements Database{
    MongoClient client;
    public MongoDB() {
        client = MongoClients.create();
    }
}
