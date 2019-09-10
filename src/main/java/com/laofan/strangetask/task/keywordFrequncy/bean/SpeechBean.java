package com.laofan.strangetask.task.keywordFrequncy.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author:pan le
 * Date:2019/9/9
 * Time:16:00
 * 录音文件返回bean
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpeechBean {
        String taskId;
        String statusCode;
        String statusText;
}
