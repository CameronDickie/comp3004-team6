package com.comp3004.educationmanager.visitor;

import java.util.Base64;

public class PDF implements FileInterface {
    private String byteString;
    private byte[] bytes;

    @Override
    public String accept(FileVisitor v) {
        return v.visitPDF(this);
    }

    @Override
    public void setFile(String bytes) {
        this.bytes = Base64.getDecoder().decode(bytes);
        this.byteString = bytes;
    }

    @Override
    public String getByteString() {
        return byteString;
    }

    @Override
    public byte[] getBytes() {
        return bytes;
    }
}

