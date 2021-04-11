package com.comp3004.educationmanager.visitor;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
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
            InputStream pptStream = new ByteArrayInputStream(file.getBytes());
            XMLSlideShow ppt = new XMLSlideShow(pptStream);
            Dimension pageSize = ppt.getPageSize();

            for(XSLFSlide s : ppt.getSlides()) {
                BufferedImage img = new BufferedImage(pageSize.width, pageSize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();
                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, pageSize.width, pageSize.height));

            }

            OutputStream byteStream = new ByteArrayOutputStream();

            return Base64.getEncoder().encodeToString(((ByteArrayOutputStream) byteStream).toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
