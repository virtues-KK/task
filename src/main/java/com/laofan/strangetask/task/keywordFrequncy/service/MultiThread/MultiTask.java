package com.laofan.strangetask.task.keywordFrequncy.service.MultiThread;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laofan.strangetask.task.keywordFrequncy.bean.JsonWords;
import com.laofan.strangetask.task.keywordFrequncy.bean.SpeechBean;
import com.laofan.strangetask.task.keywordFrequncy.bean.Words_;
import com.laofan.strangetask.task.keywordFrequncy.entity.Article;
import com.laofan.strangetask.task.keywordFrequncy.repository.AliyunParameterRepository;
import com.laofan.strangetask.task.keywordFrequncy.service.AliYunSpeechToWord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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

    private final AliyunParameterRepository aliyunParameterRepository;


    @Autowired
    public MultiTask(Provider provider, AliyunParameterRepository aliyunParameterRepository) {
        this.provider = provider;
        this.aliyunParameterRepository = aliyunParameterRepository;
    }

    @Async
    public Future<String> getVioceTransction(String filePath, Long remainTime,boolean add){
        String provide = provider.provide(filePath);
        //get title
        String title = null;
        try {
            title = URLDecoder.decode(filePath, "utf-8").split("/")[3];
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (add){
            add = provider.completeRetryTable(filePath);
        }
        if (provide == null){
            //不能给出适合的账号,结束当前的文件任务,保存文件,只能下次处理
            log.error("所有账号用量耗尽");
            provider.collectFile(filePath);
            return returnBody();
        }
        String[] split = provide.split(",");
        System.out.println(Arrays.toString(split));
        String accessKeyId = split[0];
        String accessKeySecret = split[1];
        String appKey = split[2];
        System.out.println("线程 :" + Thread.currentThread().getName());
        System.out.println("当前调用appkey : " + appKey);
        //正常处理后扣除当前账号用量
        provider.updateTime(remainTime,accessKeyId +","+accessKeySecret +","+ appKey);
        AliYunSpeechToWord aliYunSpeechToWord = new AliYunSpeechToWord(accessKeyId, accessKeySecret);
        SpeechBean resultBean = aliYunSpeechToWord.submitFileTransRequest(appKey, filePath);
        String statusCode = resultBean.getStatusCode();
        String taskId = resultBean.getTaskId();
        // 这里的用量耗尽,一般不会出现,因为给出的账号都是有剩余时长的,出现了这种情况的话需要把当前账号的时长置为0
        if ("41050001".equals(statusCode)) {
            log.error("用量耗尽" + provide);
            provider.collectFile(filePath);
            provider.updateTime(7200L,accessKeyId +","+accessKeySecret +","+ appKey);
            return returnBody();
            //出现不可预知异常,保存retry文件即可
        }else if (statusCode.startsWith("4") || statusCode.startsWith("5")) {
            provider.collectFile(filePath);
            log.error(appKey + filePath + " 此任务失败,转入二次处理区域");
            return returnBody();
        }
        String fileTransResult = aliYunSpeechToWord.getFileTransResult(taskId);

        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false).configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        JsonWords jsonWord = new JsonWords();
        try {
            if (fileTransResult == null){
                provider.collectFile(filePath);
                log.error(URLDecoder.decode(filePath,"utf-8") + "识别失败,转入二次识别区域" );
                return returnBody();
            }
            jsonWord = objectMapper.readValue(fileTransResult, JsonWords.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> collect = jsonWord.getWords().stream().map(Words_::getWord).distinct().collect(Collectors.toList());
        String content = String.join("", collect);
        Article save = provider.save(content,title);
        if (Objects.nonNull(save)){
            log.info("保存成功" + content);
        }else {
            log.error("保存失败" + content);
        }
        return returnBody();
    }

    public static Future<String> returnBody(){
        return new Future<String>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return true;
            }

            @Override
            public String get() throws InterruptedException, ExecutionException {
                return null;
            }

            @Override
            public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return null;
            }
        };
    }

}
