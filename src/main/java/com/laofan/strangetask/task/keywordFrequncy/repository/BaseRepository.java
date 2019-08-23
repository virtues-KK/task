package com.laofan.strangetask.task.keywordFrequncy.repository;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * @author panyue
 * 自定义repository的基类
 */
class BaseRepository<T,TD extends Serializable> extends SimpleJpaRepository<T,TD> {
    private final EntityManager entityManager;
    public BaseRepository(JpaEntityInformation entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public <S extends T> S save(S entity){
     entityManager.persist(entity);
     return null;
    }
}
