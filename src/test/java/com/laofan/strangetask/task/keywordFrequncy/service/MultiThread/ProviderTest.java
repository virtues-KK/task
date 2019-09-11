package com.laofan.strangetask.task.keywordFrequncy.service.MultiThread;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProviderTest {

    @Autowired
    private Provider provider;

    @Test
    public void provide() {
        String provide = provider.provide("https://video-pl.oss-cn-huhehaote.aliyuncs.com/%E7%AC%AC%E5%85%AB%E7%AB%A0%E7%AC%AC%E5%85%AD%E8%8A%82%20%E8%AE%A9%E8%94%A1%E5%AE%81%E7%9C%A9%E6%99%95%E7%9A%84%E7%BE%8E%E5%9B%BD%E5%AD%A9%E5%AD%90%E6%88%90%E5%B9%B4%E5%A4%A7%E7%A4%BC.mp3");
        System.out.println(provide);
    }
}