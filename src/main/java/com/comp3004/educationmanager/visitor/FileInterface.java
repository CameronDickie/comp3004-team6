package com.comp3004.educationmanager.visitor;

public interface FileInterface {
    byte[] accept(FileVisitor v);
    void setFile(byte[] bytes);
}
