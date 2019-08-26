package com.laofan.strangetask.task.keywordFrequncy.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 测试阿里云语音转文字
 */
@Slf4j
public class AliYunSpeechToWordTest {

    private String appKey = "mElJH70O6zjh7tbC";

    private String filePath = "https://video-pl.oss-cn-huhehaote.aliyuncs.com/%E7%AC%AC%E4%B8%80%E7%AB%A0%E7%AC%AC%E4%B8%80%E8%8A%82%20%E6%96%B0%E6%9D%A5%E7%9A%84%E7%BE%8E%E5%9B%BD%E9%82%BB%E5%B1%85%EF%BC%8C%E4%B8%8D%E7%88%B1%E6%8A%B1%E5%AD%A9%E5%AD%90.mp3";

    @Test
    public void getFileTransResult() throws IOException {
        AliYunSpeechToWord aliYunSpeechToWord = new AliYunSpeechToWord("LTAICzLVPlryaIad", "jLUlSxOaWqvNnIkh5aA7l3zPa8lZzY");
        String taskId = aliYunSpeechToWord.submitFileTransRequest(appKey, filePath);
        String fileTransResult = aliYunSpeechToWord.getFileTransResult(taskId);
        log.info(fileTransResult);
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false).configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,false).configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES,true);
        jsonWords jsonWord = objectMapper.readValue(fileTransResult, jsonWords.class);
        List<String> collect = jsonWord.getWords().stream().map(jsonWords.Words_::getWord).distinct().collect(Collectors.toList());
        collect.forEach(System.out::print);

    }

    @Test
    public void test() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false).configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,false).configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES,true);
        jsonWords jsonWord = objectMapper.readValue(new File("D:\\task\\src\\main\\resources\\jsonWords.json"), jsonWords.class);
        System.out.println(jsonWord);
        log.info(jsonWord.getWords().size() + "");
        List<String> collect = jsonWord.getWords().stream().map(jsonWords.Words_::getWord).distinct().collect(Collectors.toList());
        collect.forEach(System.out::print);
    }

}
@Data
class jsonWords {
    @JsonProperty("Words")
    private List<Words_> words;
    @JsonProperty("Sentences")
    private List<Sentences_> sentences;
    @Data
    class Sentences_ {
        Long EndTime;
        Integer SilenceDuration;
        String Text;
        Integer SpeechRate;
        Double EmotionValue;
        Long BeginTime;
        Long ChannelId;
    }
    @Data
    class Words_ {
        String Word;
        Long EndTime;
        Long BeginTime;
        Long ChannelId;
    }
}


