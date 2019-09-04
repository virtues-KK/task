package com.laofan.strangetask.task.keywordFrequncy.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author:pan le
 * Date:2019/9/2
 * Time:9:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sentences_ {
    Long EndTime;
    Integer SilenceDuration;
    String Text;
    Integer SpeechRate;
    Double EmotionValue;
    Long BeginTime;
    Long ChannelId;
}
