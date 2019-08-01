package com.laofan.strangetask.task.keywordFrequncy.repository;

import com.laofan.strangetask.task.keywordFrequncy.entity.ArticleKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ArticleKeywordRepository extends JpaRepository<ArticleKeyword,Long> {

    @Modifying
    @Query(value = "delete from ArticleKeyword a where a.articleId = ?1")
    void deleteByArticleId(Long articleId);
}
