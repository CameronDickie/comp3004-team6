package com.comp3004.educationmanager.visitor;

import org.apache.commons.io.FileUtils;

import java.io.File;

public class PPTX implements FileInterface {
    private File file;

    @Override
    public byte[] accept(FileVisitor v) {
        return v.visitPPTX(this);
    }

    @Override
    public void setFile(byte[] bytes) {
        try {
            FileUtils.writeByteArrayToFile(file, bytes);
        } catch(java.io.IOException e) {
            System.out.println("Error reading file from bytes");
        }
    }
}
