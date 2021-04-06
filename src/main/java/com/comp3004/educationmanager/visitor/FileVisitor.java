package com.comp3004.educationmanager.visitor;

public interface FileVisitor {
    byte[] visitPDF(PDF file);
    byte[] visitDOCX(DOCX file);
    byte[] visitPPTX(PPTX file);
}
