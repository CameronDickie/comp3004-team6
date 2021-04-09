//package com.comp3004.educationmanager.visitor;
//
//import org.apache.commons.io.FileUtils;
//
//import java.io.File;
//
//public class DOCX implements FileInterface {
//    private File file;
//    private byte[] bytes;
//
//    @Override
//    public byte[] accept(FileVisitor v) {
//        return v.visitDOCX(this);
//    }
//
//    @Override
//    public void setFile(byte[] bytes) {
//        try {
//            this.bytes = bytes;
//            FileUtils.writeByteArrayToFile(file, bytes);
//        } catch(java.io.IOException e) {
//            System.out.println("Error reading file from bytes");
//        }
//    }
//
//    @Override
//    public byte[] getBytes() {
//        return bytes;
//    }
//
//    @Override
//    public File getFile() {
//        return file;
//    }
//}
