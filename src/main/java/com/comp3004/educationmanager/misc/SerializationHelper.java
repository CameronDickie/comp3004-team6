package com.comp3004.educationmanager.misc;

import com.comp3004.educationmanager.observer.CourseData;
import com.comp3004.educationmanager.observer.CourseDataSerialized;

import java.io.*;
import java.nio.charset.Charset;

public class SerializationHelper {

    public void SerializationHelper() {

    }

    //Function that takes in an object and serializes it and the Serialized Object is returned so it can be stored in the database
    public Object createSerializedObject(Object obj, String type) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream( byteArrayOutputStream );
        objectOutputStream.writeObject(obj);
        objectOutputStream.close();

        if (type == "course") {
            CourseDataSerialized courseDataSerialized = new CourseDataSerialized();
            courseDataSerialized.setObj(byteArrayOutputStream.toByteArray());

            String string = new String(byteArrayOutputStream.toByteArray(), Charset.defaultCharset());

            System.out.println("SERIALIZATION STRING::: " + string);

            return courseDataSerialized;
        }
        else if (type == "user") {
            return null;
        }
        else {
            return null;
        }

    }

    //Function that takes in an object and serializes it and the byte array is returned to be stored in the Database
    public Object deserializeObject(byte[] byteArray, String type) throws IOException, ClassNotFoundException {

       if (type == "course") {
           ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
           ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

           CourseData courseData = (CourseData) objectInputStream.readObject();

           return courseData;

       }
       else if (type == "user") {
           return null;
       }
       else {
           return null;
       }


    }
}
