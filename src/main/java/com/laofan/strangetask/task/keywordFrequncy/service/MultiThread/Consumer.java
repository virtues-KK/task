package com.laofan.strangetask.task.keywordFrequncy.service.MultiThread;

import com.laofan.strangetask.task.keywordFrequncy.entity.RehandleFiles;
import com.laofan.strangetask.task.keywordFrequncy.repository.ReHandleFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
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

    @Autowired
    public Consumer(Provider provider, MultiTask multiTask, ReHandleFileRepository reHandleFileRepository) {
        this.provider = provider;
        this.multiTask = multiTask;
        this.reHandleFileRepository = reHandleFileRepository;
    }
    /**
     * 语音转换
     */
    @Scheduled(cron = "0 26 16 * * ?")
    public void transction(){
        boolean done = false;
        List<String> list = this.initMap();
        List<String> collect1 = reHandleFileRepository.findAll().stream().map(RehandleFiles::getRetryFile).collect(Collectors.toList());
        // 优先处理昨天剩下的文件
        for (String s : collect1) {
            Long fileTime = provider.getFileTime(provider.decoder(s));
            multiTask.getVioceTransction(s, fileTime,true);
        }
        for (String s : list) {
            Long fileTime = provider.getFileTime(provider.decoder(s));
            multiTask.getVioceTransction(s, fileTime,false);
        }
    }

    /**
     * 初始化oss文件地址
     * @return oss文件集合
     */
    public List<String> initMap() {
        File file = new File(sourthFilePath);
        Arrays.stream(Objects.requireNonNull(file.listFiles())).forEach(file1 -> {
            log.info(file1.getName());
            String name = file1.getName();
            try {
                String filePath =ossHead + URLEncoder.encode(name, "utf-8");
                filePaths.add(filePath);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
        return filePaths;
    }
}
