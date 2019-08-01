package com.laofan.strangetask.task.keywordFrequncy.service;

import com.laofan.strangetask.task.keywordFrequncy.entity.Article;
import com.laofan.strangetask.task.keywordFrequncy.entity.Frequency;
import com.laofan.strangetask.task.keywordFrequncy.entity.Keyword;
import com.laofan.strangetask.task.keywordFrequncy.repository.ArticleKeywordRepository;
import com.laofan.strangetask.task.keywordFrequncy.repository.ArticleRepository;
import com.laofan.strangetask.task.keywordFrequncy.repository.FrequencyRepository;
import com.laofan.strangetask.task.keywordFrequncy.repository.KeywordRepository;
import com.laofan.strangetask.task.keywordFrequncy.utils.FileCharsetConverter;
import com.laofan.strangetask.task.keywordFrequncy.utils.NLPUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * author:pan le
 * Date:2019/7/31
 * Time:9:58
 * 使用阿里云nlp 标注词性并保存
 */
@Service
public class AliYunNLP {

    private final ArticleKeywordRepository articleKeywordRepository;

    private final ArticleRepository articleRepository;

    private final FrequencyRepository frequencyRepository;

    private final KeywordRepository keywordRepository;

    public AliYunNLP(ArticleKeywordRepository articleKeywordRepository, ArticleRepository articleRepository, FrequencyRepository frequencyRepository, KeywordRepository keywordRepository) {
        this.articleKeywordRepository = articleKeywordRepository;
        this.articleRepository = articleRepository;
        this.frequencyRepository = frequencyRepository;
        this.keywordRepository = keywordRepository;
    }

    public void analysisArticle(String filepath) throws Exception{
        File[] files = new File(filepath).listFiles();
        List<String> keywords;
        assert files != null;
        assert files.length != 0;
        for (File file : files) {
            //转换编码
//            FileCharsetConverter.convert(file,"ISO8859_1","GBK");
//            FileCharsetConverter.convert(file,"GBK","UTF-8");
            String articleName = file.getName();
            List<Frequency> frequencies  = new ArrayList<>();
            List<String> list = NLPUtils.keywords(file);
            Map<String, Long> collect = list.stream().distinct().collect(Collectors.toMap(str -> str, s -> list.stream().filter(s::equals).count()));
            //不重复创建文章以及其他表格数据
            if (articleRepository.findByName(articleName).isPresent()){
                continue;
            }
            Article article = Article.builder()
                    .name(articleName)
                    .build();
            //save dir article
            Article article1 = articleRepository.save(article);
            keywords = keywordRepository.findAll().stream().map(Keyword::getName).collect(Collectors.toList());
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
    }
}
