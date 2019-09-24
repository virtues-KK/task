package com.laofan.strangetask.task.keywordFrequncy.service;

import com.laofan.strangetask.task.keywordFrequncy.entity.ConvertFailedEntity;
import com.laofan.strangetask.task.keywordFrequncy.repository.ConvertFailedRepository;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import lombok.extern.slf4j.Slf4j;
import org.omg.IOP.Codec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Objects;

/**
 * author:pan le
 * Date:2019/9/24
 * Time:10:45
 *  转换音频格式和采样率
 */
@Slf4j
@Service
public class AudioEncoder {

    @Autowired
    private ConvertFailedRepository convertFailedRepository;

    /**
     * minimal bitrate only
     */
    private static final Integer bitrate = 256000;
    /**
     * 2 for stereo ,1 for mono
     */
    private static final Integer channels = 2;
    /**
     * for quality
     */
    private static final Integer sampleRate = 16000;
    private AudioAttributes attributes = new AudioAttributes();
    private EncodingAttributes encodingAttributes = new EncodingAttributes();
    private Encoder encoder = new Encoder();
    private File source = new File("");
    private File target = new File("");

    public AudioEncoder() {
        attributes.setBitRate(bitrate);
        attributes.setChannels(channels);
        attributes.setSamplingRate(sampleRate);
    }

    public void m4aTomp3(File source, File target) {
        encodingAttributes.setFormat("mp3");
        attributes.setCodec("libmp3lame");
        encodingAttributes.setAudioAttributes(attributes);
        try {
            encoder.encode(source, target, encodingAttributes);
        } catch (EncoderException e) {
            e.printStackTrace();
        }
    }

    public void convert() {
        String filePath = "C:\\Users\\sunwukong\\Desktop\\200G_MP3";
        File sourceFile = new File("C:\\Users\\sunwukong\\Desktop\\200G");
        for (File file : Objects.requireNonNull(sourceFile.listFiles())) {
            log.info("任务进度" + file.getName());
            for (File listFile : Objects.requireNonNull(file.listFiles())) {
                File targetFile = new File(filePath + "\\" + file.getName() + "\\" + listFile.getName().replaceAll("m4a", "mp3"));
                try {
                    encodingAttributes.setFormat("mp3");
                    attributes.setCodec("libmp3lame");
                    encodingAttributes.setAudioAttributes(attributes);
                    encoder.encode(listFile, targetFile, encodingAttributes);
                    log.info("任务成功: " + targetFile.getPath());
                }catch (Exception e){
                    e.printStackTrace();
                    convertFailedRepository.save(ConvertFailedEntity.builder().FileName(targetFile.getPath()).build());
                    log.error("转换失败,保存" + targetFile.getPath());
                }
            }
        }
    }
}