package com.laofan.strangetask.task.keywordFrequncy.service;

import com.laofan.strangetask.task.keywordFrequncy.entity.Frequency;
import com.laofan.strangetask.task.keywordFrequncy.repository.ArticleRepository;
import com.laofan.strangetask.task.keywordFrequncy.repository.FrequencyRepository;
import com.laofan.strangetask.task.keywordFrequncy.repository.KeywordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 计算关键字的tf_idf值
 *
 * @author vourtous
 */
@Service
@Slf4j
public class TfidfService {

    private final FrequencyRepository frequencyRepository;

    private final KeywordRepository keywordRepository;

    private final ArticleRepository articleRepository;

    private static List<Frequency> frequencies;

    @Autowired
    public TfidfService(FrequencyRepository frequencyRepository, KeywordRepository keywordRepository, ArticleRepository articleRepository) {
        this.frequencyRepository = frequencyRepository;
        this.keywordRepository = keywordRepository;
        this.articleRepository = articleRepository;
    }

    /**
     * 计算tf_idf值
     *
     * @param keywordId
     * @param articleId
     */
    public double tf_idf(Long keywordId, Long articleId) {
        //获取数据库所有文章的词数
        long count = frequencies.stream().map(Frequency::getFrequency).count();
        //该词在本文中的频率
        Long keywordFrequency = frequencyRepository.findByKeywordAndArticle(keywordId, articleId);
        //获取逆文本频率
        double log = Math.log(count / (keywordFrequency + 1));
        double logs = log * keywordFrequency;
        return logs;

    }

    /**
     * 准备全库频率数据
     */
    @PostConstruct
    public void prepare(){
        frequencies = frequencyRepository.findAll();
    }


}
