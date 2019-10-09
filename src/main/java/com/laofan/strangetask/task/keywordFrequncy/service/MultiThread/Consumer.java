package com.laofan.strangetask.task.keywordFrequncy.service.MultiThread;

import com.laofan.strangetask.task.keywordFrequncy.entity.RehandleFiles;
import com.laofan.strangetask.task.keywordFrequncy.repository.AliyunParameterRepository;
import com.laofan.strangetask.task.keywordFrequncy.repository.ReHandleFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * author:pan le
 * Date:2019/9/9
 * Time:15:07
 */
@Component
@Slf4j
public class Consumer {

    /**
     * 需要二次处理的文件数据
     */
    public static HashSet<String> reHandleList = new HashSet<>();

    private final Provider provider;

    private final MultiTask multiTask;

    /**
     * oss 文件地址
     */
    private List<String> filePaths = new ArrayList<>();

    /**
     * 本地待处理文件夹
     */
    @Value("${aliyun.sourthFilePath}")
    private String sourthFilePath;

    @Value("${aliyun.ossHead}")
    private String ossHead;

    private final ReHandleFileRepository reHandleFileRepository;

    private final AliyunParameterRepository aliyunParameterRepository;

    @Autowired
    public Consumer(Provider provider, MultiTask multiTask, ReHandleFileRepository reHandleFileRepository, AliyunParameterRepository aliyunParameterRepository) {
        this.provider = provider;
        this.multiTask = multiTask;
        this.reHandleFileRepository = reHandleFileRepository;
        this.aliyunParameterRepository = aliyunParameterRepository;
    }

    /**
     * 语音转换
     * 1,重试文件和第一次文件之间的重复问题
     * 2,重置账号时长的问题
     */
//    @Scheduled(cron = "0 26 16 * * ?")
    @Transactional
    public void transction() {
        //每天重置账号的剩余时长
        aliyunParameterRepository.recoverTime();
        List<String> list = this.initMap();
        List<String> collect1 = reHandleFileRepository.findAll().stream().map(RehandleFiles::getRetryFile).collect(Collectors.toList());
        // 优先处理昨天剩下的文件
        for (String s : collect1) {
            Long fileTime = provider.getFileTime(provider.decoder(s));
            multiTask.getVioceTransction(s, fileTime, true);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(8);
        for (String s : list) {
            Long fileTime = provider.getFileTime(provider.decoder(s));
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    multiTask.getVioceTransction(s, fileTime, false);
                }
            });
        }
    }

    /**
     * 初始化oss文件地址
     *
     * @return oss文件集合
     */
    public List<String> initMap() {
        File file = new File("A:\\200G_mp3");
        Arrays.stream(Objects.requireNonNull(file.listFiles())).forEach(file1 -> {
            for (File listFile : Objects.requireNonNull(file1.listFiles())) {
//                log.info(listFile.getPath().split("200G_mp3")[1]);
                String titleName = file1.getName();
                try {
                    String filePath = ossHead + URLEncoder.encode(titleName, "utf-8") + "/" + URLEncoder.encode(listFile.getName(), "utf-8");
                    filePaths.add(filePath);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        return filePaths;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String s = "https://video-pl.oss-cn-huhehaote.aliyuncs.com/%E4%B8%8E%E5%AD%A9%E5%AD%90%E5%A5%BD%E5%A5%BD%E8%AF%B4%E8%AF%9D/issue%2005%20%E4%B8%8D%E5%BA%94%E8%BF%87%E5%BA%A6%E6%89%B9%E5%88%A4%EF%BC%8C%E8%A6%81%E5%BC%95%E5%AF%BC%E5%AD%A9%E5%AD%90%E8%87%AA%E5%B7%B1%E8%A7%A3%E5%86%B3%E9%97%AE%E9%A2%98.mp3";
        String encode = URLDecoder.decode(s, "utf-8");
        System.out.println(encode);
    }

}
