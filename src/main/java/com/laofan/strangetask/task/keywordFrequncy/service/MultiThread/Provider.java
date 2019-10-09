package com.laofan.strangetask.task.keywordFrequncy.service.MultiThread;

import com.laofan.strangetask.task.keywordFrequncy.bean.SpeechParameterBean;
import com.laofan.strangetask.task.keywordFrequncy.entity.AliyunParameterEntity;
import com.laofan.strangetask.task.keywordFrequncy.entity.Article;
import com.laofan.strangetask.task.keywordFrequncy.entity.RehandleFiles;
import com.laofan.strangetask.task.keywordFrequncy.repository.AliyunParameterRepository;
import com.laofan.strangetask.task.keywordFrequncy.repository.ArticleRepository;
import com.laofan.strangetask.task.keywordFrequncy.repository.ReHandleFileRepository;
import it.sauronsoftware.jave.Encoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


/**
 * author:pan le
 * Date:2019/9/9
 * Time:15:03
 *  安全提供账号参数
 */
@Component
@Slf4j
@Transactional
public class Provider {

    private final SpeechParameterBean speechParameterBean;

    private List<String> parameter;

    CopyOnWriteArrayList<String> pa;

    @Value("${aliyun.ossHead}")
    private String ossHead;

    @Value("${aliyun.sourthFilePath}")
    private String sourthFilePath;

    private final AliyunParameterRepository aliyunParameterRepository;

    private final ReHandleFileRepository reHandleFileRepository;


    private final ArticleRepository articleRepository;

    @Autowired
    public Provider(SpeechParameterBean speechParameterBean, AliyunParameterRepository aliyunParameterRepository, ReHandleFileRepository reHandleFileRepository, ArticleRepository articleRepository) {
        this.speechParameterBean = speechParameterBean;
        this.aliyunParameterRepository = aliyunParameterRepository;
        this.reHandleFileRepository = reHandleFileRepository;
        this.articleRepository = articleRepository;
    }

    /**
     * init parameter table
     */
    @PostConstruct
    public void init(){
        try {
            speechParameterBean.getParameter().stream()
                    .map(AliyunParameterEntity::gennerate)
                    .collect(Collectors.toList()).forEach(aliyunParameterRepository::save);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 线程安全的情况下每次调用给出一个随机的账号
     * @return 随机账号
     */
    String provide(){
        log.error(parameter.size() +"");
        if (parameter.size() == 0){
            return null;
        }
        Collections.shuffle(parameter);
        return parameter.get(0);
    }

    /**
     * 根据文件时长给出适合的账号
     * @param fileName 文件名
     */
    String provide(String fileName){
        String localFileName = this.decoder(fileName);
        Long fileTime = this.getFileTime(localFileName);
        return aliyunParameterRepository.findMinByTime(fileTime);
    }

    /**
     * 获取文件时长
     * @param fileName
     * @return
     */
    public Long getFileTime(String fileName){
        Long second = 0L;
        try{
            Encoder encoder = new Encoder();
            second= encoder.getInfo(new File(fileName)).getDuration() / 1000;
        }catch (Exception e){
            e.printStackTrace();
        }
        return second;
    }



    public String decoder(String fileName){
        String localFileName = null;
        try {
            localFileName = sourthFilePath + URLDecoder.decode(fileName.split(ossHead)[1],"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return localFileName;
    }



    public void remove(String str){
        if (parameter.contains(str)){
            parameter.remove(str);
        }else {
            log.info(" too busy for do nothing");
        }
    }

    public void collectFile(String filePath){
        reHandleFileRepository.save(new RehandleFiles(null,filePath));
    }

    public Boolean isValid(String variable){
        return parameter.contains(variable);
    }

    /**
     * update remainTime
     * @param remainTime
     * @param parameter
     */
    @Transactional(rollbackFor = NullPointerException.class)
    public void updateTime(Long remainTime,String parameter){
        aliyunParameterRepository.updateRemainTime(parameter,remainTime);
    }

    public boolean completeRetryTable(String filePath){
        AtomicBoolean error = new AtomicBoolean(false);
        reHandleFileRepository.findByRetryFile(filePath).ifPresent(s -> {
            reHandleFileRepository.deleteByRetryFile(s);
            error.set(true);
        });
        //true- 重试文件,已删除 false-新文件
        return error.get();
    }
    /**
     * save article
     */
    public Article save(String content,String title){
        return articleRepository.save(Article.builder().content(content).title(title).build());
    }


}
