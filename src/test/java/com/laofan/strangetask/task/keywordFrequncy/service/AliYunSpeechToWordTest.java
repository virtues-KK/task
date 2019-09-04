package com.laofan.strangetask.task.keywordFrequncy.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laofan.strangetask.task.keywordFrequncy.bean.JsonWords;
import com.laofan.strangetask.task.keywordFrequncy.bean.Words_;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 测试阿里云语音转文字
 */
@Slf4j
@Component
public class AliYunSpeechToWordTest {

    private String filePath = "https://video-pl.oss-cn-huhehaote.aliyuncs.com/%E7%AC%AC%E4%B8%80%E7%AB%A0%E7%AC%AC%E4%B8%80%E8%8A%82%20%E6%96%B0%E6%9D%A5%E7%9A%84%E7%BE%8E%E5%9B%BD%E9%82%BB%E5%B1%85%EF%BC%8C%E4%B8%8D%E7%88%B1%E6%8A%B1%E5%AD%A9%E5%AD%90.mp3";



    /**
     * 测试多线程识别音频文件
     *
     * @throws IOException
     */

//    @Test
//    public void multithreadingFileTransResult() {
//        this.initMap();
//        map.forEach((k, V) -> {
//            String[] split = V.split(",");
//            this.getFileTransResult(k, split[0], split[1],split[2]);
//        });
//    }

    @Test
    public void getFileTransResult() throws IOException {
        AliYunSpeechToWord aliYunSpeechToWord = new AliYunSpeechToWord("LTAICzLVPlryaIad", "jLUlSxOaWqvNnIkh5aA7l3zPa8lZzY");
        String taskId = aliYunSpeechToWord.submitFileTransRequest("mElJH70O6zjh7tbC", filePath);
        String fileTransResult = aliYunSpeechToWord.getFileTransResult(taskId);
        log.info(fileTransResult);
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false).configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        JsonWords jsonWord = objectMapper.readValue(fileTransResult, JsonWords.class);
        List<String> collect = jsonWord.getWords().stream().map(Words_::getWord).distinct().collect(Collectors.toList());
        collect.forEach(System.out::print);
    }

    @Test
    public void test() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false).configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        JsonWords jsonWord = objectMapper.readValue(new File("D:\\task\\src\\main\\resources\\jsonWords.json"), JsonWords.class);
        System.out.println(jsonWord);
        log.info(jsonWord.getWords().size() + "");
        List<String> collect = jsonWord.getWords().stream().map(Words_::getWord).distinct().collect(Collectors.toList());
        collect.forEach(System.out::print);
    }

//    public void initMap() {
//        String value1 = accessKeyId + "," + accessKeySecret + "," + appKey;
////        String value2 = accessKeyId1 + "," + accessKeySecret1 + "," + appKey1;
//        String value3 = accessKeyId2 + "," + accessKeySecret2 + "," + appKey2;
//        File file = new File(sourceFilePath);
//        Arrays.stream(Objects.requireNonNull(file.listFiles())).forEach(file1 -> {
//            System.out.println(file1.getName());
//            String name = file1.getName().split("\\.")[0];
//            try {
//                String filePath ="https://video-pl.oss-cn-huhehaote.aliyuncs.com/" + URLEncoder.encode(name, "utf-8") + ".mp3";
//                System.out.println(filePath);
//                filePaths.add(filePath);
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        });
//        for (int i = 1; i < filePaths.size()-1; i++) {
//            switch (i % 2) {
//                case 0:
//                    map.put(filePaths.get(i), value1);
//                    break;
//                case 1:
//                    map.put(filePaths.get(i), value3);
//                    break;
////                case 2:
////                    map.put(filePaths.get(i), value3);
////                    break;
//            }
//        }
//        log.info(map.size()+ "function init works");
//    }
//
//    @Async
//    public void getFileTransResult(String filePath, String accessKeyId, String accessKeySecret,String appKey) {
//        System.out.println(Thread.currentThread().getName());
//        AliYunSpeechToWord aliYunSpeechToWord = new AliYunSpeechToWord(accessKeyId, accessKeySecret);
//        String taskId = aliYunSpeechToWord.submitFileTransRequest(appKey, filePath);
//        String fileTransResult = aliYunSpeechToWord.getFileTransResult(taskId);
//        log.info(fileTransResult);
//        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false).configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
//        jsonWords jsonWord = null;
//        try {
//            jsonWord = objectMapper.readValue(fileTransResult, jsonWords.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        List<String> collect = jsonWord.getWords().stream().map(jsonWords.Words_::getWord).distinct().collect(Collectors.toList());
//        collect.forEach(System.out::print);
//
//    }

}

//@Data
//class jsonWords {
//    @JsonProperty("Words")
//    private List<Words_> words;
//    @JsonProperty("Sentences")
//    private List<Sentences_> sentences;
//
//    @Data
//    class Sentences_ {
//        Long EndTime;
//        Integer SilenceDuration;
//        String Text;
//        Integer SpeechRate;
//        Double EmotionValue;
//        Long BeginTime;
//        Long ChannelId;
//    }
//
//    @Data
//    class Words_ {
//        String Word;
//        Long EndTime;
//        Long BeginTime;
//        Long ChannelId;
//    }
//}


