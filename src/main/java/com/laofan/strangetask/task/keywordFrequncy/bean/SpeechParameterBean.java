package com.laofan.strangetask.task.keywordFrequncy.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("aliyun")
@Data
public class SpeechParameterBean {
    public List<String> parameter;
}
