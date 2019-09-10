package com.laofan.strangetask.task.keywordFrequncy.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@EnableConfigurationProperties(SpeechParameterBean.class)
@ConfigurationProperties(prefix = "aliyun")
public class SpeechParameterBean {
    public List<String> parameter;
}
