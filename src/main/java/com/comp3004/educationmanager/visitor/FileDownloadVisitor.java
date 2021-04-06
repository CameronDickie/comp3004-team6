package com.comp3004.educationmanager.visitor;

public class FileDownloadVisitor implements FileVisitor {
    @Override
    public byte[] visitPDF(PDF file) {
        return file.getBytes();
    }

    @Override
    public byte[] visitDOCX(DOCX file) {
        return file.getBytes();
    }

    @Override
    public byte[] visitPPTX(PPTX file) {
        return file.getBytes();
    }
}
