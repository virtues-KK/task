package com.laofan.strangetask.task.keywordFrequncy.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class OssService {

//    private static String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
    private static String endpoint = "http://oss-cn-huhehaote.aliyuncs.com";
    private static String accessKeyId = "LTAIzwTbXiWKFq8P";
    private static String accessKeySecret = "1SVb4EvdoCRuiw8dZggZVL2kdKr8KC";
//    private static String bucketName = "8kt-sp";
    private static String bucketName = "video-pl";
    private static String firstKey = "my-first-key";
    private static String objectName = "speeches";

    /**
     * 文件上传
     */
    public void fileUpload(String files) throws FileNotFoundException {
        OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        if (oss.doesBucketExist(bucketName)) {
            System.out.println("已经创建了bucket: " + bucketName);
        } else {
            oss.createBucket(bucketName);
        }
        // print information from bucket
        BucketInfo info = oss.getBucketInfo(bucketName);
        System.out.println("Bucket " + bucketName + "的信息如下：");
        System.out.println("\t数据中心：" + info.getBucket().getLocation());
        System.out.println("\t创建时间：" + info.getBucket().getCreationDate());
        System.out.println("\t用户标志：" + info.getBucket().getOwner());
        File[] fs = new File(files).listFiles();
        for (int i = 0; i < fs.length; i++) {
            try{
                oss.putObject(bucketName, fs[i].getName(), fs[i]);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        oss.shutdown();
    }


    /**
     * 分片上传
     */
    public void ossUploadFileService() {
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
//            if (ossClient.doesBucketExist(bucketName)) {
//                System.out.println("已经创建了bucket: " + bucketName);
//            } else {
//                ossClient.createBucket(bucketName);
//            }
//            // print information from bucket
//            BucketInfo info = ossClient.getBucketInfo(bucketName);
//            System.out.println("Bucket " + bucketName + "的信息如下：");
//            System.out.println("\t数据中心：" + info.getBucket().getLocation());
//            System.out.println("\t创建时间：" + info.getBucket().getCreationDate());
//            System.out.println("\t用户标志：" + info.getBucket().getOwner());
            //分片上传文件
            InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, objectName);
            InitiateMultipartUploadResult result = ossClient.initiateMultipartUpload(request);
            String uploadId = result.getUploadId();
            //分片上传
            List<PartETag> partETags = new ArrayList<>();
            final Long partSize = 1 * 1024L * 1024L;
            final File sampleFile = new File("<localFile>");
            Long fileLength = sampleFile.length();
            int partCount = (int) (fileLength / partSize);
            if (fileLength % partSize != 0) {
                partCount++;
            }
            for (int i = 0; i < partCount; i++) {
                long startPos = i * partSize;
                long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
                InputStream instream = new FileInputStream(sampleFile);
                // 跳过已经上传的分片。
                instream.skip(startPos);
                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(bucketName);
                uploadPartRequest.setKey(objectName);
                uploadPartRequest.setUploadId(uploadId);
                uploadPartRequest.setInputStream(instream);
                // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100KB。
                uploadPartRequest.setPartSize(curPartSize);
                // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出这个范围，OSS将返回InvalidArgument的错误码。
                uploadPartRequest.setPartNumber(i + 1);
                // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
                UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
                // 每次上传分片之后，OSS的返回结果会包含一个PartETag。PartETag将被保存到partETags中。
                partETags.add(uploadPartResult.getPartETag());
            }
            /* 步骤3：完成分片上传。
             */
            // 排序。partETags必须按分片号升序排列。
            Collections.sort(partETags, new Comparator<PartETag>() {
                @Override
                public int compare(PartETag p1, PartETag p2) {
                    return p1.getPartNumber() - p2.getPartNumber();
                }
            });
            // 在执行该操作时，需要提供所有有效的partETags。OSS收到提交的partETags后，会逐一验证每个分片的有效性。当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
            CompleteMultipartUploadRequest completeMultipartUploadRequest =
                    new CompleteMultipartUploadRequest(bucketName, objectName, uploadId, partETags);
            ossClient.completeMultipartUpload(completeMultipartUploadRequest);

            // 关闭OSSClient。
            ossClient.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
