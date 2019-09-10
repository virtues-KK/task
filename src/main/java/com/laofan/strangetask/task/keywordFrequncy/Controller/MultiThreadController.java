package com.laofan.strangetask.task.keywordFrequncy.Controller;

import com.laofan.strangetask.task.keywordFrequncy.service.MultiThread.Consumer;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * author:pan le
 * Date:2019/8/30
 * Time:14:15
 */
@RestController
@RequestMapping("multiThread")
public class MultiThreadController {

    @Autowired
    private Consumer consumer;

    @GetMapping("multiTest")
    public void test(){
        consumer.transction();
    }

}
