package com.laofan.strangetask.task.keywordFrequncy.utils;

import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import vavi.sound.pcm.resampling.ssrc.SSRC;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * author:pan le
 * Date:2019/8/19
 * Time:10:49
 * 转换音频采样率
 */
public class JssrcUtils {

    private String targetPath = "C:\\Users\\sunwukong\\Desktop\\targetSpeech";

    public void jssrcSwitch(String filePath){
        File file = new File(filePath);
        for (File listFile : file.listFiles()) {
            this.switchOne(listFile,new File("C:\\Users\\sunwukong\\Desktop\\targetSpeech\\第八章第三节 听猫“叫春”，那跟公鸡打鸣不一样.m4a"));
        }
    }

    public void switchOne(File file,File tagetFile){
        try {
            FileInputStream fileInputStream = FileUtils.openInputStream(file);
            FileOutputStream fileOutputStream = FileUtils.openOutputStream(tagetFile);
            new SSRC(fileInputStream, fileOutputStream, 44100, 44100, 2, 2, 1, Integer.MAX_VALUE, 0, 0, true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
