package com.laofan.strangetask.task.keywordFrequncy.service;

import com.laofan.strangetask.task.keywordFrequncy.entity.Article;
import com.laofan.strangetask.task.keywordFrequncy.repository.ArticleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Collectors;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TfidfServiceTest {

    @Autowired
    private TfidfService tfidfService;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void tf_idf() {
        articleRepository.findAll().stream().map(Article::getId).collect(Collectors.toList()).forEach(c->tfidfService.tf_idf(c));
    }
}