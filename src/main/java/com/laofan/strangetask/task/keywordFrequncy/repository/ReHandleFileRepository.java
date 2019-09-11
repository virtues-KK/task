package com.laofan.strangetask.task.keywordFrequncy.repository;

import com.laofan.strangetask.task.keywordFrequncy.entity.RehandleFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReHandleFileRepository extends JpaRepository<RehandleFiles,Long> {

    /**
     * @param retryFile
     * 查找再处理文件是否存在数据库
     * @return
     */
    @Query
    Optional<String> findByRetryFile(String retryFile);


    /**
     * @param filePath
     * 存在就处理此文件并删除
     */
    @Modifying
    @Query("delete from RehandleFiles where retryFile = ?1")
    void deleteByRetryFile(String filePath);
}
