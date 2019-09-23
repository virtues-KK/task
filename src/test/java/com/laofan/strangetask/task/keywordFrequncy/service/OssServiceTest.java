package com.laofan.strangetask.task.keywordFrequncy.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.Assert.*;

/**
 * 上传部分音频资料
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class OssServiceTest {

    @Autowired
    private OssService ossService;

    @Test
    public void fileUpload() throws FileNotFoundException {
        ossService.fileUpload("C:\\Users\\sunwukong\\Desktop\\oss_");
    }
}