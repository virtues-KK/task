package com.laofan.strangetask.task.keywordFrequncy.Controller;

import com.laofan.strangetask.task.keywordFrequncy.service.JiebaService;
import com.laofan.strangetask.task.keywordFrequncy.service.TfidfService;
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

    private final TfidfService tfidfService;

    @Autowired
    public AnalysisController(JiebaService jiebaService, TfidfService tfidfService) {
        this.jiebaService = jiebaService;
        this.tfidfService = tfidfService;
    }

    /**
     * 获取文章关键字
     * @param filePath
     * @throws Exception
     */
    @GetMapping("analysisArticle")
    public void analysisArticle(String filePath) throws Exception {
        jiebaService.frequency(filePath);
    }

    /**
     * 获取文章推荐关键字
     */
    public void getIf_IdfKeyword(){
//        tfidfService
    }
}
