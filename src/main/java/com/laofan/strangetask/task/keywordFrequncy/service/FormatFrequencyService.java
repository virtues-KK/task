package com.laofan.strangetask.task.keywordFrequncy.service;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;
import org.bytedeco.javacv.FrameFilter;
import org.bytedeco.javacv.FrameGrabber;
import org.springframework.stereotype.Service;
import vavi.sound.pcm.resampling.ssrc.SSRC;

import javax.sound.sampled.*;
import java.io.*;
import java.util.Map;

/**
 * author:pan le
 * Date:2019/9/18
 * Time:14:09
 * @author sunwukong
 */
@Service
public class FormatFrequencyService {

    public static void main(String[] args) throws Exception {
        File f = new File("C:\\Users\\sunwukong\\Desktop\\第八章第六节 让蔡宁眩晕的美国孩子成年大礼.m4a");
//        File out = new File("C:\\Users\\sunwukong\\Desktop\\biggift.m4a");
//        FileInputStream fileInputStream = new FileInputStream(f);
//        FileOutputStream fileOutputStream = new FileOutputStream(out);
//        new SSRC(fileInputStream,fileOutputStream,44100,8000,2,2,1,Integer.MAX_VALUE,0,0,true);

        AudioFileFormat audioFileFormat = AudioSystem.getAudioFileFormat(f);
        AudioFormat format = audioFileFormat.getFormat();
        float sampleRate = format.getSampleRate();
        int frameSize = format.getFrameSize();
        AudioFormat.Encoding encoding = format.getEncoding();
        int channels = format.getChannels();
        float frameRate = format.getFrameRate();
        int sampleSizeInBits = format.getSampleSizeInBits();
        boolean bigEndian = format.isBigEndian();
        AudioFormat audioFormat = new AudioFormat(encoding,(float)16000,sampleSizeInBits,channels,frameSize,frameRate,bigEndian);

        AudioInputStream audioInputStream = new AudioInputStream(new FileInputStream(f),audioFormat, AudioSystem.getAudioInputStream(f).getFrameLength());
        AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE,new File("C:\\Users\\sunwukong\\Desktop\\成年大礼.wav"));
        System.out.println(sampleRate);
        RandomAccessFile rdf = new RandomAccessFile(f, "rw");
        System.out.println("sample rate: " + toInt(read(rdf, 24, 4)));

    }

    public void format() throws Exception {
        File f = new File("C:\\Users\\sunwukong\\Desktop\\200G\\好妈妈-每天3分钟帮孩子全脑开发\\01.冰箱上贴什么？-帮助孩子练习做决策.m4a");
        RandomAccessFile rdf = null;
        rdf = new RandomAccessFile(f, "rw");
        write(rdf, 22);

        System.out.println("audio size: " + toInt(read(rdf, 4, 4))); // 音频文件大小

        System.out.println("audio format: " + toShort(read(rdf, 20, 2))); // 音频格式，1-PCM

        System.out.println("num channels: " + toShort(read(rdf, 22, 2))); // 1-单声道；2-双声道

        System.out.println("sample rate: " + toInt(read(rdf, 24, 4))); // 采样率、音频采样级别

        System.out.println("byte rate: " + toInt(read(rdf, 28, 4))); // 每秒波形的数据量

        System.out.println("block align: " + toShort(read(rdf, 32, 2))); // 采样帧的大小

        System.out.println("bits per sample: " + toShort(read(rdf, 34, 2))); // 采样位数

        rdf.close();
    }

    public static int toInt(byte[] b) {
        return ((b[3] << 24) + (b[2] << 16) + (b[1] << 8) + (b[0] << 0));
    }

    public static short toShort(byte[] b) {
        return (short) ((b[1] << 8) + (b[0] << 0));
    }

    public static byte[] read(RandomAccessFile rdf, int pos, int length) throws IOException {
        rdf.seek(pos);
        byte result[] = new byte[length];
        for (int i = 0; i < length; i++) {
            result[i] = rdf.readByte();
        }
        return result;
    }

    public static void write(RandomAccessFile rdf, int pos) throws IOException {
        rdf.seek(pos);
        byte[] b = {02,20};
        rdf.write(b);
    }


}
