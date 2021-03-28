package com.comp3004.educationmanager.misc;

import java.io.*;

public class Serialization {

    public void SerializationHelper() {

    }

    public byte[] serialize(Object o) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream( byteArrayOutputStream );
        objectOutputStream.writeObject(o);
        objectOutputStream.close();

        return byteArrayOutputStream.toByteArray();
    }

    public Object deserialize(byte[] object) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(object);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return objectInputStream.readObject();
    }
}
