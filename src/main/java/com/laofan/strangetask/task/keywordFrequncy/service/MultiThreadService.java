package com.laofan.strangetask.task.keywordFrequncy.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laofan.strangetask.task.keywordFrequncy.bean.JsonWords;
import com.laofan.strangetask.task.keywordFrequncy.bean.Words_;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * author:pan le
 * Date:2019/8/30
 * Time:14:16
 */
@Service
@Slf4j
@ConfigurationProperties(prefix ="aliyun")
@Getter
@Setter
public class MultiThreadService {

    private List<String> filePaths = new ArrayList<>();

    private String sourceFilePath = "C:\\Users\\sunwukong\\Desktop\\targetSpeech - 副本";

    /**
     * 需处理的总文件集合
     */
    private List<String> waitHandleList = new ArrayList<>();

    /**
     * 异常文件集合
     */
    private List<String> exceptionList = new ArrayList<>();

    /**
     * 正常二次处理文件集合
     */
    private List<String> handleAgainList = new ArrayList<>();

    /**
     * 识别系统参数集合
     */
    private List<String> parameter = new ArrayList<>();

    public List<String> initMap() {
        File file = new File(sourceFilePath);
        Arrays.stream(Objects.requireNonNull(file.listFiles())).forEach(file1 -> {
            System.out.println(file1.getName());
            String name = file1.getName();
            try {
                String filePath ="https://video-pl.oss-cn-huhehaote.aliyuncs.com/" + URLEncoder.encode(name, "utf-8");
                System.out.println(filePath);
                filePaths.add(filePath);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
        return filePaths;
    }

    public ConcurrentHashMap<String,String> initConcurrentHashMap(List<String> filePaths){
        /**
         * k:filepath
         * v: accessKetId +","+ accessKeySecret + ","+appKey
         */
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
        filePaths.forEach(f->{
            map.put(f, parameter.get(filePaths.indexOf(f)%parameter.size()));
        });
        log.info(map.size()+ "function init works");
        return map;
    }


    @Async
    public void getFileTransResult(String filePath, String accessKeyId, String accessKeySecret,String appKey) {
        System.out.println(Thread.currentThread().getName());
        AliYunSpeechToWord aliYunSpeechToWord = new AliYunSpeechToWord(accessKeyId, accessKeySecret);
        SpeechBean resultBean = aliYunSpeechToWord.submitFileTransRequest(appKey, filePath);
        // statusCode 41050001 当日免费时长已经用完,应该回归本次任务文件,并且关闭此账号
        if ("41050001".equals(resultBean.getStatusCode())){
            handleAgainList.add(filePath);
            return;
        }
        String fileTransResult = aliYunSpeechToWord.getFileTransResult(taskId);
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false).configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        JsonWords jsonWord = new JsonWords();
        try {
            if (fileTransResult == null){
                log.error(URLDecoder.decode(filePath,"utf-8") + "识别失败" );
                return;
            }
            jsonWord = objectMapper.readValue(fileTransResult, JsonWords.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> collect = jsonWord.getWords().stream().map(Words_::getWord).distinct().collect(Collectors.toList());
        log.info(collect.stream().collect(Collectors.joining()));
    }
}