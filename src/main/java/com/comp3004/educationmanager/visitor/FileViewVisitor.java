package com.comp3004.educationmanager.visitor;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;

import java.io.*;

public class FileViewVisitor implements FileVisitor {
    @Override
    public byte[] visitPDF(PDF file) {
        return file.getBytes();
    }

    @Override
    public byte[] visitDOCX(DOCX file) {
        try {
            InputStream docStream = new FileInputStream(file.getFile());
            XWPFDocument doc = new XWPFDocument(docStream);
            PdfOptions options = PdfOptions.getDefault();
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            PdfConverter.getInstance().convert(doc, byteStream, options);
            return byteStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public byte[] visitPPTX(PPTX file) {
        try {
            InputStream docStream = new FileInputStream(file.getFile());
            XWPFDocument doc = new XWPFDocument(docStream);
            PdfOptions options = PdfOptions.getDefault();
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            PdfConverter.getInstance().convert(doc, byteStream, options);
            return byteStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
