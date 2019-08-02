package com.laofan.strangetask.task.keywordFrequncy.repository;

import com.laofan.strangetask.task.keywordFrequncy.entity.Frequency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FrequencyRepository extends JpaRepository<Frequency,Long> {

    @Query("select f.frequency from Frequency f where article.id =?2 and keyword.id = ?1")
    Long findByKeywordAndArticle(Long keywordId, Long articleId);

    @Query("select f.keyword.id from Frequency f where f.article.id = ?1")
    List<Long> findKeywordByArtcle(Long articleId);

    @Modifying
    @Query("update Frequency set tf_idfValue = ?1 where keyword.id = ?2 and article.id = ?3")
    void updateIfidfValue(double logs, Long keyword, Long articleId);
}
