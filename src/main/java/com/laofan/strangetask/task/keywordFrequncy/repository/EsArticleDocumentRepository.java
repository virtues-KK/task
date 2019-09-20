package com.laofan.strangetask.task.keywordFrequncy.repository;

import com.laofan.strangetask.task.keywordFrequncy.entity.EsArticleDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

/**
 * @author sunwukong
 */
public interface EsArticleDocumentRepository extends ElasticsearchCrudRepository<EsArticleDocument,Long> {
}
