package com.comp3004.educationmanager.visitor;

public interface FileVisitor {
    void visitPDF(PDF file);
    void visitDOCX(DOCX file);
    void visitPPTX(PPTX file);
}
