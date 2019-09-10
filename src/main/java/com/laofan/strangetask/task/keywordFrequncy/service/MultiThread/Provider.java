package com.laofan.strangetask.task.keywordFrequncy.service.MultiThread;

import com.laofan.strangetask.task.keywordFrequncy.bean.SpeechParameterBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.laofan.strangetask.task.keywordFrequncy.service.MultiThread.Consumer.reHandleList;

/**
 * author:pan le
 * Date:2019/9/9
 * Time:15:03
 *  安全提供账号参数
 */
@Component
@Slf4j
public class Provider {

    private final SpeechParameterBean speechParameterBean;

    private List<String> parameter;

    CopyOnWriteArrayList<String> pa;

    @Autowired
    public Provider(SpeechParameterBean speechParameterBean) {
        this.speechParameterBean = speechParameterBean;
    }

    @PostConstruct
    public void init(){
        parameter = speechParameterBean.getParameter();
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

    public void remove(String str){
        if (parameter.contains(str)){
            parameter.remove(str);
        }else {
            log.info(" too busy for do nothing");
        }
    }

    public void collectFile(String filePath){
        reHandleList.add(filePath);
    }

    public Boolean isValid(String variable){
        return parameter.contains(variable);
    }

}
