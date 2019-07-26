package com.laofan.strangetask.task.keywordFrequncy.Controller;

import com.laofan.strangetask.task.keywordFrequncy.service.JiebaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author:pan le
 * Date:2019/7/24
 * Time:16:34
 *
 * @author sunwukong
 */
@RestController
@RequestMapping("analysis")
public class AnalysisController {

    private final JiebaService jiebaService;

    @Autowired
    public AnalysisController(JiebaService jiebaService) {
        this.jiebaService = jiebaService;
    }

    @GetMapping("analysisArticle")
    public void analysisArticle(String filePath) throws Exception {
        jiebaService.frequency(filePath);
    }
}
