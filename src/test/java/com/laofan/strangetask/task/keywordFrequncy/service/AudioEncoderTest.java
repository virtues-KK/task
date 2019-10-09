package com.laofan.strangetask.task.keywordFrequncy.service;

import com.laofan.strangetask.task.keywordFrequncy.repository.ConvertFailedRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AudioEncoderTest {

    @Autowired
    private AudioEncoder audioEncoder;

    @Autowired
    private OssService ossService;

    @Autowired
    private ConvertFailedRepository convertFailedRepository;

    @Test
    public void convert() throws FileNotFoundException {
        String convert = audioEncoder.convert();
    }
}