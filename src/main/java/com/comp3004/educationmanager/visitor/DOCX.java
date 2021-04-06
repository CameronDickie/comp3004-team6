package com.comp3004.educationmanager.visitor;

import java.io.File;

public class DOCX implements FileInterface {
    private File file;

    @Override
    public byte[] accept(FileVisitor v) {
        return v.visitDOCX(this);
    }

    @Override
    public void setFile(byte[] bytes) {

    }
}
