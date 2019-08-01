package com.laofan.strangetask.task.keywordFrequncy.Controller;

import com.laofan.strangetask.task.keywordFrequncy.service.AliYunNLP;
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


    private final TfidfService tfidfService;

    private final AliYunNLP aliYunNLP;

    @Autowired
    public AnalysisController(TfidfService tfidfService, AliYunNLP aliYunNLP) {
        this.tfidfService = tfidfService;
        this.aliYunNLP = aliYunNLP;
    }

    /**
     * 获取文章关键字
     * @param filePath
     * @throws Exception
     */
    @GetMapping("analysisArticle")
    public void analysisArticle(String filePath) throws Exception {
        aliYunNLP.analysisArticle(filePath);
    }

    /**
     * 获取文章推荐关键字
     */
    public void getIf_IdfKeyword(){
    }
}
