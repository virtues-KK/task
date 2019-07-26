package com.laofan.strangetask.task.keywordFrequncy.service;

import com.laofan.strangetask.task.keywordFrequncy.entity.Article;
import com.laofan.strangetask.task.keywordFrequncy.entity.Frequency;
import com.laofan.strangetask.task.keywordFrequncy.entity.Keyword;
import com.laofan.strangetask.task.keywordFrequncy.repository.ArticleRepository;
import com.laofan.strangetask.task.keywordFrequncy.repository.FrequencyRepository;
import com.laofan.strangetask.task.keywordFrequncy.repository.KeywordRepository;
import com.laofan.strangetask.task.keywordFrequncy.utils.JiebaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * author:pan le
 * Date:2019/7/19
 * Time:11:17
 * @author sunwukong
 */
@Slf4j
@Service
public class JiebaService {

    private static List<String> keywords;

    private final FrequencyRepository frequencyRepository;

    private final KeywordRepository keywordRepository;

    private final ArticleRepository articleRepository;

    @Autowired
    public JiebaService(FrequencyRepository frequencyRepository, KeywordRepository keywordRepository, ArticleRepository articleRepository) {
        this.frequencyRepository = frequencyRepository;
        this.keywordRepository = keywordRepository;
        this.articleRepository = articleRepository;
    }

    private String fileName = "C:\\Users\\sunwukong\\Desktop\\new.txt";

    public void frequency(String fileName) throws Exception {
        List<String> list = JiebaUtils.stringFromPython(fileName);
        Map<String, Long> collect = list.stream().distinct().collect(Collectors.toMap(str -> str, s -> list.stream().filter(s::equals).count()));
        List<Frequency> frequencies = new ArrayList<>();
        //单个只保存一篇文章
        Article article = Article.builder()
                .name("孩子才是副作用呢!")
                .build();
        //save dir article
        Article article1 = articleRepository.save(article);
        for (String c : collect.keySet()) {
            // save dir keyword
            Keyword keyword1;
            if (!keywords.contains(c)) {
                Keyword keyword = Keyword.builder()
                        .name(c)
                        .build();
                keyword1 = keywordRepository.save(keyword);
                keywords.add(c);
            } else {
                keyword1 = keywordRepository.findByName(c);
            }
            //save frequency
            Frequency frequency = Frequency.builder()
                    .frequency(collect.get(c))
                    .article(article1)
                    .keyword(keyword1)
                    .build();
            frequencies.add(frequency);
        }
        frequencyRepository.saveAll(frequencies);
    }

    /**
     * 准备当前数据
     */
    @PostConstruct
    public void initialization() {
        keywords = keywordRepository.findAll().stream().map(Keyword::getName).collect(Collectors.toList());
        log.info("当前的keyword数量为" + keywords.size());
    }
}
