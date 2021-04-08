package com.comp3004.educationmanager.visitor;

import java.io.File;

public interface FileInterface {
    byte[] accept(FileVisitor v);
    void setFile(byte[] bytes);
    byte[] getBytes();
}
