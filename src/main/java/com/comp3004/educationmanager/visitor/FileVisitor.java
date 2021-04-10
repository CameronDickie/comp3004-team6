package com.comp3004.educationmanager.visitor;

public interface FileVisitor {
    String visitPDF(PDF file);
    String visitDOCX(DOCX file);
    String visitPPTX(PPTX file);
}
