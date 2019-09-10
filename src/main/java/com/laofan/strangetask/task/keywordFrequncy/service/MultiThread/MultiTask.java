package com.laofan.strangetask.task.keywordFrequncy.service.MultiThread;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laofan.strangetask.task.keywordFrequncy.bean.JsonWords;
import com.laofan.strangetask.task.keywordFrequncy.bean.SpeechBean;
import com.laofan.strangetask.task.keywordFrequncy.bean.Words_;
import com.laofan.strangetask.task.keywordFrequncy.service.AliYunSpeechToWord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * author:pan le
 * Date:2019/9/9
 * Time:14:59
 * 多线程跑转换接口
 */
@Component
@Slf4j
public class MultiTask {

    private final Provider provider;

    @Autowired
    public MultiTask(Provider provider) {
        this.provider = provider;
    }

    @Async
    public void getVioceTransction(String filePath){
        String provide = provider.provide();
        if (provide == null){
            log.error("所有账号用量耗尽");
            return;
        }
        if (!provider.isValid(provide)){
            log.error("用量耗尽" + provide);
            provider.collectFile(filePath);
            provider.remove(provide);
            this.getVioceTransction(filePath);
            return;
        }
        String[] split = provide.split(",");
        System.out.println(Arrays.toString(split));
        String accessKeyId = split[0];
        String accessKeySecret = split[1];
        String appKey = split[2];
        System.out.println("线程 :" + Thread.currentThread().getName());
        System.out.println("当前调用appkey : " + appKey);
        AliYunSpeechToWord aliYunSpeechToWord = new AliYunSpeechToWord(accessKeyId, accessKeySecret);
        SpeechBean resultBean = aliYunSpeechToWord.submitFileTransRequest(appKey, filePath);
        String statusCode = resultBean.getStatusCode();
        String taskId = resultBean.getTaskId();
        if ("41050001".equals(statusCode)) {
            log.error("用量耗尽" + provide);
            provider.collectFile(filePath);
            provider.remove(provide);
            this.getVioceTransction(filePath);
            return;
        }else if (statusCode.startsWith("4") || statusCode.startsWith("5")) {
            provider.collectFile(filePath);
            log.error(appKey + filePath + " 此任务失败,转入二次处理区域");
            this.getVioceTransction(filePath);
            return;
        }
        String fileTransResult = aliYunSpeechToWord.getFileTransResult(taskId);
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false).configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        JsonWords jsonWord = new JsonWords();
        try {
            if (fileTransResult == null){
                provider.collectFile(filePath);
                log.error(URLDecoder.decode(filePath,"utf-8") + "识别失败,转入二次识别区域" );
                provider.remove(provide);
                this.getVioceTransction(filePath);
                return;
            }
            jsonWord = objectMapper.readValue(fileTransResult, JsonWords.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> collect = jsonWord.getWords().stream().map(Words_::getWord).distinct().collect(Collectors.toList());
        log.info(String.join("", collect));
        return;
    }
}
