package com.laofan.strangetask.task.keywordFrequncy.service.MultiThread;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ConsumerTest {

    @Autowired
    private Consumer consumer;

    @Test
    public void transction() {
        consumer.transction();
    }
}