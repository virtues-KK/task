package com.laofan.strangetask.task.keywordFrequncy.service;

import com.laofan.strangetask.task.keywordFrequncy.entity.EsArticleDocument;
import com.laofan.strangetask.task.keywordFrequncy.repository.EsArticleDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author sunwukong
 */
@Service
public class EsArticleDocumentService {
    private final EsArticleDocumentRepository esArticleDocumentRepository;

    @Autowired
    public EsArticleDocumentService(EsArticleDocumentRepository esArticleDocumentRepository) {
        this.esArticleDocumentRepository = esArticleDocumentRepository;
    }

    public void esArticleSave(){
        esArticleDocumentRepository.save(EsArticleDocument.builder().articleContent("我过不下去了啊").articleName("钱难挣屎难吃").keyWord("le").id(1L).build());
        esArticleDocumentRepository.findById(1L).ifPresent(System.out::println);
    }

}
