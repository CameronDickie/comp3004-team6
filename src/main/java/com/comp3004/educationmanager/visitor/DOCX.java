package com.comp3004.educationmanager.visitor;

public class DOCX implements FileInterface {
    private byte[] bytes;

    @Override
    public byte[] accept(FileVisitor v) {
        return v.visitDOCX(this);
    }

    @Override
    public void setFile(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public byte[] getBytes() {
        return bytes;
    }
}

