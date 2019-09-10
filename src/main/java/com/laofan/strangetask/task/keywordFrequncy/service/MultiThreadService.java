//package com.laofan.strangetask.task.keywordFrequncy.service;
//
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.MapperFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.laofan.strangetask.task.keywordFrequncy.bean.JsonWords;
//import com.laofan.strangetask.task.keywordFrequncy.bean.SpeechParameterBean;
//import com.laofan.strangetask.task.keywordFrequncy.bean.Words_;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.extern.slf4j.Slf4j;
//import org.omg.CORBA.Environment;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//import java.io.File;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.net.URLDecoder;
//import java.net.URLEncoder;
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.CopyOnWriteArrayList;
//import java.util.stream.Collectors;
//
///**
// * author:pan le
// * Date:2019/8/30
// * Time:14:16
// * @author sunwukong
// */
//@Service
//@Slf4j
//@Getter
//@Setter
////@EnableConfigurationProperties(MultiThread.class)
////@ConfigurationProperties("aliyun")
//public class MultiThreadService {
//
//    /**
//     * k:filepath
//     * v: accessKetId +","+ accessKeySecret + ","+appKey
//     */
//    ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
//
//    private List<String> filePaths = new ArrayList<>();
//
//    private String sourceFilePath = "C:\\Users\\sunwukong\\Desktop\\targetSpeech - 副本";
//
//    /**
//     * 需处理的总文件集合
//     */
//    private List<String> waitHandleList = new ArrayList<>();
//
//    /**
//     * 异常文件集合
//     */
//    private List<String> exceptionList = new ArrayList<>();
//
//    /**
//     * 正常二次处理文件集合
//     */
//    private List<String> handleAgainList = new ArrayList<>();
//
//    /**
//     * 日用量耗尽账号集合
//     */
//    private List<String> overUseList = new ArrayList<>();
//
//    @Autowired
//    private SpeechParameterBean speechParameterBean;
//
//    /**
//     * 识别系统参数集合
//     */
//    private List<String> parameter;
//
//    private ConcurrentHashMap parameterConcurrentMap = new ConcurrentHashMap();
//
//
//    /**
//     * provider app variable
//     */
//    public String provider(){
//        CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList(parameter);
//        return (String) copyOnWriteArrayList.stream().findAny().get();
//    }
//
//
//    /**
//     * 集成service方法
//     */
//    public void getFileTransResult() {
////        List<String> list = this.initMap();
//        if (overUseList.size() != parameter.size()){
////            list.forEach(this::getFileTransResult);
////            for (int i = 0; i < list.size(); i++) {
////                this.getFileTransResult(list.get(i));
////            }
////        }
//
//    }
//
////    public List<String> initMap() {
////        parameter = speechParameterBean.parameter;
////        File file = new File(sourceFilePath);
////        Arrays.stream(Objects.requireNonNull(file.listFiles())).forEach(file1 -> {
////            log.info(file1.getName());
////            String name = file1.getName();
////            try {
////                String filePath ="https://video-pl.oss-cn-huhehaote.aliyuncs.com/" + URLEncoder.encode(name, "utf-8");
////                filePaths.add(filePath);
////            } catch (UnsupportedEncodingException e) {
////                e.printStackTrace();
////            }
////        });
////        return filePaths;
////    }
//
////    public ConcurrentHashMap<String,String> initMap(List<String> filePaths){
////        parameter = speechParameterBean.parameter;
////        filePaths.forEach(f->{
////            map.put(f, parameter.get(filePaths.indexOf(f)%parameter.size()));
////        });
////        log.info(map.size()+ "function init works");
////        return map;
////    }
//
//
//    @Async
//    public void getFileTransResult(String filePath){
//        String[] split = this.provider().split(",");
//        String accessKeyId = split[0];
//        String accessKeySecret = split[1];
//        String appKey = split[2];
//        if (overUseList.contains(appKey)){
//            //用量耗尽账号，应该直接结束任务,当前账号绑定的任务在本日已经无法进行，等待下一天重新分配任务
//            log.error("今日当前账号时长用尽,明天再来吧!");
//            return;
//        }
//        System.out.println("线程 :" + Thread.currentThread().getName());
//        System.out.println("当前调用appkey" + appKey);
//        AliYunSpeechToWord aliYunSpeechToWord = new AliYunSpeechToWord(accessKeyId, accessKeySecret);
//        SpeechBean resultBean = aliYunSpeechToWord.submitFileTransRequest(appKey, filePath);
//        String statusCode = resultBean.getStatusCode();
//        String taskId = resultBean.getTaskId();
//        // statusCode 41050001 当日免费时长已经用完,应该回归本次任务文件,加入用尽用量的集合
//        if ("41050001".equals(statusCode)){
//            handleAgainList.add(filePath);
//            overUseList.add(appKey);
//            log.error(appKey+":"+ "此账号当日时长已耗尽");
//            return;
//        }else if (statusCode.startsWith("4") || statusCode.startsWith("5")){
//            handleAgainList.add(filePath);
//            log.error(appKey + filePath + " 此任务失败,转入二次处理区域");
//            return;
//        }
//        String fileTransResult = aliYunSpeechToWord.getFileTransResult(taskId);
//        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false).configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
//        JsonWords jsonWord = new JsonWords();
//        try {
//            if (fileTransResult == null){
//                handleAgainList.add(filePath);
//                log.error(URLDecoder.decode(filePath,"utf-8") + "识别失败,转入二次识别区域" );
//                return;
//            }
//            jsonWord = objectMapper.readValue(fileTransResult, JsonWords.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //完成一个任务之后，删除此任务
//        map.remove(filePath);
//        List<String> collect = jsonWord.getWords().stream().map(Words_::getWord).distinct().collect(Collectors.toList());
//        log.info(String.join("", collect));
//    }
//}