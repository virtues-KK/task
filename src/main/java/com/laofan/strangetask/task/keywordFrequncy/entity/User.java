package com.laofan.strangetask.task.keywordFrequncy.entity;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * author:pan le
 * Date:2019/7/19
 * Time:9:47
 * @author sunwukong
 */
@Entity
@EnableJpaAuditing
@EntityListeners(value = AuditingEntityListener.class)
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String username;

    @Column(name = "password")
    private String password;

    @CreatedDate
    @Type(type = "java.time.LocalDateTime")
    private LocalDateTime createTime;
}
