package com.comp3004.educationmanager.visitor;

import java.io.File;

public interface FileInterface {
    String accept(FileVisitor v);
    void setFile(String bytes);
    String getByteString();
    byte[] getBytes();
}
