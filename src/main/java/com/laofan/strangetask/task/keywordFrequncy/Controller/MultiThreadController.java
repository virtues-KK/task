package com.laofan.strangetask.task.keywordFrequncy.Controller;

import com.laofan.strangetask.task.keywordFrequncy.service.MultiThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author:pan le
 * Date:2019/8/30
 * Time:14:15
 */
@RestController
@RequestMapping("multiThread")
public class MultiThreadController {
    @Autowired
    private MultiThreadService multiThreadService;

    @GetMapping("multiTest")
    public void multiThreadTest(){
        List<String> list = multiThreadService.initMap();
        ConcurrentHashMap<String, String> stringStringConcurrentHashMap = multiThreadService.initMap(list);
        stringStringConcurrentHashMap.forEach((k,v) ->{
            String[] strings = v.split(",");
            multiThreadService.getFileTransResult(k,strings[0],strings[1],strings[2]);
         });
    }
}
