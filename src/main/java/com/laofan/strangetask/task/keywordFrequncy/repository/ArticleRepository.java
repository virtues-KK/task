package com.laofan.strangetask.task.keywordFrequncy.repository;

import com.laofan.strangetask.task.keywordFrequncy.entity.Article;
import com.laofan.strangetask.task.keywordFrequncy.entity.Frequency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article,Long> {
    @Query(value = "from Article a where a.name = ?1")
    Optional<Article> findByName(String s);
}
