package com.comp3004.educationmanager.visitor;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
import java.util.Base64;

public class FileViewVisitor implements FileVisitor {
    @Override
    public String visitPDF(PDF file) {
        return file.getByteString();
    }

    @Override
    public String visitDOCX(DOCX file) {
        try {
            InputStream docStream = new ByteArrayInputStream(file.getBytes());
            XWPFDocument doc = new XWPFDocument(docStream);
            PdfOptions options = PdfOptions.create();
            OutputStream byteStream = new ByteArrayOutputStream();
            PdfConverter.getInstance().convert(doc, byteStream, options);
            return Base64.getEncoder().encodeToString(((ByteArrayOutputStream) byteStream).toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String visitPPTX(PPTX file) {
        try {
            InputStream docStream = new ByteArrayInputStream(file.getBytes());
            XWPFDocument doc = new XWPFDocument(docStream);
            PdfOptions options = PdfOptions.getDefault();
            OutputStream byteStream = new ByteArrayOutputStream();
            PdfConverter.getInstance().convert(doc, byteStream, options);
            return Base64.getEncoder().encodeToString(((ByteArrayOutputStream) byteStream).toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
