package com.laofan.strangetask.task.keywordFrequncy.service.MultiThread;

import com.laofan.strangetask.task.keywordFrequncy.bean.SpeechParameterBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * author:pan le
 * Date:2019/9/9
 * Time:15:07
 * 方法消费方
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

    @Autowired
    public Consumer(Provider provider, MultiTask multiTask) {
        this.provider = provider;
        this.multiTask = multiTask;
    }

    Boolean vioceTransction = false;

    /**
     * 语音转换
     */
    public void transction(){
        List<String> list = this.initMap();
        for (String s : list) {
            multiTask.getVioceTransction(s);
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
                String filePath ="https://video-pl.oss-cn-huhehaote.aliyuncs.com/" + URLEncoder.encode(name, "utf-8");
                filePaths.add(filePath);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
        return filePaths;
    }


}
