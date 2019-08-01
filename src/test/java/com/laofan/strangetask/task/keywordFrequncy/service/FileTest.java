package com.laofan.strangetask.task.keywordFrequncy.service;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * author:pan le
 * Date:2019/8/1
 * Time:9:34
 */
public class FileTest {

    public static void main(String[] args) throws Exception {
        File listFiles = new File("C:\\Users\\sunwukong\\Desktop\\testif_idf");
//        File[] files = listFile.listFiles();
//        File listFile = new File("C:\\Users\\sunwukong\\Desktop\\2 “假面”留学生-姜东序.docx");
//            bufferedReader.lines().forEach(FileTest::accept);
//            System.out.println(streamReader.getEncoding());
//            bufferedReader.lines().forEach(System.out::println);
        // poi读取文件
        for (File listFile : listFiles.listFiles()) {
            if (listFile.getName().endsWith(".doc")) {
                InputStream fis = new FileInputStream(listFile);
                WordExtractor wordExtractor = new WordExtractor(fis);
                for (String s : wordExtractor.getParagraphText()) {
                    System.out.println(s);
                }
            }
            if (listFile.getName().endsWith(".docx")) {
                OPCPackage opcPackage = POIXMLDocument.openPackage(listFile.getPath());
                XWPFDocument xwpfDocument = new XWPFDocument(opcPackage);
                for (XWPFParagraph paragraph : xwpfDocument.getParagraphs()) {
                    System.out.println(paragraph.getText());
                }
            }
        }
    }
    private static void accept(String s) {
        String str = new String(s.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
//            String str1 = new String(str.getBytes("GBK"), StandardCharsets.UTF_8);
        System.out.println(str);
    }
}
