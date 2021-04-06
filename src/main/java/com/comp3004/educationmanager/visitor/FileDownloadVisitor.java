package com.comp3004.educationmanager.visitor;

public class FileDownloadVisitor implements FileVisitor {
    @Override
    public byte[] visitPDF(PDF file) {
        return null;
    }

    @Override
    public byte[] visitDOCX(DOCX file) {
        return null;
    }

    @Override
    public byte[] visitPPTX(PPTX file) {
        return null;
    }
}
