package com.laofan.strangetask.task.keywordFrequncy.service;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.laofan.strangetask.task.keywordFrequncy.entity.ArticleKeyword;
import com.laofan.strangetask.task.keywordFrequncy.entity.Frequency;
import com.laofan.strangetask.task.keywordFrequncy.entity.Keyword;
import com.laofan.strangetask.task.keywordFrequncy.repository.ArticleKeywordRepository;
import com.laofan.strangetask.task.keywordFrequncy.repository.ArticleRepository;
import com.laofan.strangetask.task.keywordFrequncy.repository.FrequencyRepository;
import com.laofan.strangetask.task.keywordFrequncy.repository.KeywordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 计算关键字的tf_idf值
 *
 * @author vourtous
 */
@Service
@Slf4j
@Transactional
public class TfidfService {

    private final FrequencyRepository frequencyRepository;

    private final KeywordRepository keywordRepository;

    private final ArticleRepository articleRepository;

    private static List<Frequency> frequencies;

    private final ArticleKeywordRepository articleKeywordRepository;

    @PersistenceContext
    private EntityManager entityManager;

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
        this.prepare();

        for (Long keyword : keywords) {//获取数据库所有文章的词数
            long count = frequencies.stream().map(Frequency::getFrequency).reduce(Long::sum).get();
            log.info(count + "词库中文章的总数");
            //该词在文库中的频率
            Keyword keyword1 = keywordRepository.findById(keyword).get();
            Long keywordFrequency = frequencies.stream().filter(frequency -> frequency.getKeyword().getId().equals(keyword))
                    .map(Frequency::getFrequency).reduce(Long::sum).get();
            //该词在本文中的频率
            Long keywordFrequencyInArticle = frequencyRepository.findByKeywordAndArticle(keyword, articleId);
            //获取逆文本频率
            double log = Math.log(count / (keywordFrequency + 1));
            //获取if-idf值
            double logs = log * keywordFrequencyInArticle;
            map.put(keyword, logs);
            //update frequency tf_idfValue
//            StringBuilder stringBuilder = new StringBuilder();
//            String sql = "update Frequency set tf_idfValue =";
//            stringBuilder.append(sql);
//            stringBuilder.append("?1 where keyword.id = ?2 and article.id = ?3");
//            entityManager.createQuery(stringBuilder.toString()).setParameter(1, logs).setParameter(2, keyword).setParameter(3, articleId).executeUpdate();
//            stringBuilder = null;
            frequencyRepository.updateIfidfValue(logs, keyword, articleId);
        }


        //保存前五的推荐值的keyword
        LinkedHashMap<Long, Double> linkedHashMap = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

//        LinkedHashMap linkedHashMap = this.valueSortMap(map);
        Iterator iterator = linkedHashMap.entrySet().iterator();
        //每次都会删除相关的数据,新增数据
        articleKeywordRepository.deleteByArticleId(articleId);
        int i = 0;
        while (iterator.hasNext() && i < 5) {
            Map.Entry<Long, Double> next = (Map.Entry<Long, Double>) iterator.next();
            articleKeywordRepository.save(ArticleKeyword.builder()
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
//    @PostConstruct
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
//                if (o1.getValue() == null && o2.getValue() == null) {
//                    return 0;
//                } else if (o1.getValue() == null && o2.getValue() != null) {
//                    return 1;
//                } else if (o2.getValue() == null && o1.getValue() != null) {
//                    return -1;
//                }
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


