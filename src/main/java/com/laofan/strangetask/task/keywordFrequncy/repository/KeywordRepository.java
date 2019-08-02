package com.laofan.strangetask.task.keywordFrequncy.repository;

import com.laofan.strangetask.task.keywordFrequncy.entity.Frequency;
import com.laofan.strangetask.task.keywordFrequncy.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword,Long> {
    Keyword findByName(String name);

    @Query(value = "select k.name from Keyword k")
    List<String> findAllName();
}
