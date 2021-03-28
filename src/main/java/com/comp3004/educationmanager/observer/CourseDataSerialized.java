package com.comp3004.educationmanager.observer;

import javax.persistence.*;

@Entity
public class CourseDataSerialized implements java.io.Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @Column(name = "obj")
    @Lob
    byte[] obj;

    @Column(name = "serializedString")
    String serializedString;

    public void setObj(byte[] obj) {
        this.obj = obj;
    }

    public byte[] getObj() {
        return this.obj;
    }

    public void setSerializedString(String serializedString) {

        this.serializedString = serializedString;
    }

    public String getSerializedString() {
        return this.serializedString;
    }
}
