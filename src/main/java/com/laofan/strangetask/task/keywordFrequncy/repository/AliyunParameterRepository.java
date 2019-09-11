package com.laofan.strangetask.task.keywordFrequncy.repository;

import com.laofan.strangetask.task.keywordFrequncy.entity.AliyunParameterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * author:pan le
 * Date:2019/9/10
 * Time:15:54
 */
public interface AliyunParameterRepository extends JpaRepository<AliyunParameterEntity,Long> {

    @Query
    String findMinByTime(Long remainTime);

    @Modifying
    @Query(value = "UPDATE parameter as a SET remain_time = (a.remain_time - ?2) where a.parameter =?1 ",nativeQuery = true)
    void updateRemainTime(String parameter,Long time);
}
