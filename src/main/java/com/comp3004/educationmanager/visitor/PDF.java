package com.comp3004.educationmanager.visitor;

import java.io.File;

public class PDF implements FileInterface {
    private File file;

    @Override
    public byte[] accept(FileVisitor v) {
        return v.visitPDF(this);
    }

    @Override
    public void setFile(byte[] bytes) {

    }
}
