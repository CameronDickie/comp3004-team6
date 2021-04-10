package com.comp3004.educationmanager.visitor;

public class FileDownloadVisitor implements FileVisitor {
    @Override
    public String visitPDF(PDF file) {
        return file.getByteString();
    }

    @Override
    public String visitDOCX(DOCX file) {
        return file.getByteString();
    }

    @Override
    public String visitPPTX(PPTX file) {
        return file.getByteString();
    }
}
