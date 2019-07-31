package com.laofan.strangetask.task.keywordFrequncy.service;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.laofan.strangetask.task.keywordFrequncy.entity.ArtcleKeyword;
import com.laofan.strangetask.task.keywordFrequncy.entity.Frequency;
import com.laofan.strangetask.task.keywordFrequncy.repository.ArticleKeywordRepository;
import com.laofan.strangetask.task.keywordFrequncy.repository.ArticleRepository;
import com.laofan.strangetask.task.keywordFrequncy.repository.FrequencyRepository;
import com.laofan.strangetask.task.keywordFrequncy.repository.KeywordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;


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

    private final ArticleKeywordRepository articleKeywordRepository;

    @Autowired
    public TfidfService(FrequencyRepository frequencyRepository, KeywordRepository keywordRepository, ArticleRepository articleRepository, ArticleKeywordRepository articleKeywordRepository) {
        this.frequencyRepository = frequencyRepository;
        this.keywordRepository = keywordRepository;
        this.articleRepository = articleRepository;
        this.articleKeywordRepository = articleKeywordRepository;
    }

    /**
     * 计算tf_idf值，保存推荐关键字值
     *
     * @param articleId
     */
    public void tf_idf(Long articleId) {
        List<Long> keywords = frequencyRepository.findKeywordByArtcle(articleId);
        Map<Long, Double> map = new HashMap<>();
        keywords.forEach(keyword -> {
            //获取数据库所有文章的词数
            long count = frequencies.stream().map(Frequency::getFrequency).count();
            //该词在本文中的频率
            Long keywordFrequency = frequencyRepository.findByKeywordAndArticle(keyword, articleId);
            //获取逆文本频率
            double log = Math.log(count / (keywordFrequency + 1));
            //获取if-idf值
            double logs = log * keywordFrequency;
            map.put(keyword, logs);
        });
        //保存前五的推荐值的keyword
        LinkedHashMap linkedHashMap = this.valueSortMap(map);
        Iterator iterator = linkedHashMap.entrySet().iterator();
        int i = 0;
        while (iterator.hasNext() && i < 4) {
            Map.Entry<Long, Double> next = (Map.Entry<Long, Double>) iterator.next();
            articleKeywordRepository.save(ArtcleKeyword.builder()
                    .articleId(articleId)
                    .keywordId(next.getKey())
                    .tf_idf(next.getValue())
                    .build());
            i++;
        }
    }


    /**
     * 准备全库频率数据
     */
    @PostConstruct
    public void prepare() {
        frequencies = frequencyRepository.findAll();
    }


    /**
     * 获取一个根据value降序排序的map
     *
     * @return
     */
    private LinkedHashMap valueSortMap(Map oldMap) {
        List<Map.Entry<Long, Double>> list = new ArrayList<>(oldMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Long, Double>>() {
            @Override
            public int compare(Map.Entry<Long, Double> o1, Map.Entry<Long, Double> o2) {
                return (int) (o2.getValue() - o1.getValue());
            }
        });
        LinkedHashMap<Long, Double> map = new LinkedHashMap<>();
        for (int i = 0; i < list.size(); i++) {
            map.put(list.get(i).getKey(), list.get(i).getValue());
        }
        return map;
    }


}


