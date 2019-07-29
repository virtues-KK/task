package com.laofan.strangetask.task.keywordFrequncy.repository;

import com.laofan.strangetask.task.keywordFrequncy.entity.Frequency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FrequencyRepository extends JpaRepository<Frequency,Long> {

    @Query("select Frequency.frequency from Frequency where Frequency.article.id =?2 and Frequency .keyword.id = ?1")
    Long findByKeywordAndArticle(Long keywordId, Long articleId);

}
