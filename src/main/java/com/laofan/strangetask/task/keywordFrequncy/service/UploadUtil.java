package com.laofan.strangetask.task.keywordFrequncy.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @Author: zhongxin
 * @Date: 2018/8/13 0013 17:00
 */
public class UploadUtil {

    private static String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
    private static String accessKeyId = "LTAIzwTbXiWKFq8P";
    private static String accessKeySecret = "1SVb4EvdoCRuiw8dZggZVL2kdKr8KC";
    private static String bucketName = "8kt-sp";
    private static String firstKey = "my-first-key";
    private static String objectName = "speeches";


    public static void main(String[] args) {
        UploadUtil.putObject(new File("D:\\demoSpeechFile\\ynotchinese.mp3"), "gambit/");
    }

    /**
     * location   列子  article/      gambit/   specialtopic/
     *
     * @param file
     * @param location
     * @return
     */
    public static String putObject(File file, String location) {
        if (!file.exists()) {
            return null;
        }
        String url = null;
        try {
            String path = file.getAbsolutePath();
            String[] paths = path.split("[.]");
            String fileType = paths[paths.length - 1];

            OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            InputStream in = null;

            in = new FileInputStream(file);
            String type = "image/jpeg";
            if (UploadUtil.contentType(fileType) == type) {
                BufferedImage image = ImageIO.read(in);
                int width = 200;
                int height = 150;
                if (image.getHeight() > height || image.getWidth() > width) {
                    return null;
                }
            }
            ObjectMetadata objectMetadata = new ObjectMetadata();
//            objectMetadata.setContentType(UploadUtil.contentType(fileType));
            objectMetadata.setContentType("audio/mp3");
            objectMetadata.setCacheControl("no-cache");
            String fileName = file.getName();

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, in, objectMetadata);
            ossClient.putObject(putObjectRequest);
            //上传成功再返回的文件路径
            url = endpoint.replaceFirst("http://", "http://" + bucketName + ".") + "/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(url);
        return url;
    }

    /**
     * 判断文件类型
     *
     * @param fileType
     * @return
     */
    private static String contentType(String fileType) {
        fileType = fileType.toLowerCase();
        String contentType = "";
        switch (fileType) {
            case "bmp":
                contentType = "image/bmp";
                break;
            case "gif":
                contentType = "image/gif";
                break;
            case "png":
            case "jpeg":
            case "jpg":
                contentType = "image/jpeg";
                break;
            case "html":
                contentType = "text/html";
                break;
            case "txt":
                contentType = "text/plain";
                break;
            case "vsd":
                contentType = "application/vnd.visio";
                break;
            case "ppt":
            case "pptx":
                contentType = "application/vnd.ms-powerpoint";
                break;
            case "doc":
            case "docx":
                contentType = "application/msword";
                break;
            case "xml":
                contentType = "text/xml";
                break;
            case "mp4":
                contentType = "video/mp4";
                break;
            default:
                contentType = "application/octet-stream";
                break;
        }
        return contentType;
    }


}
