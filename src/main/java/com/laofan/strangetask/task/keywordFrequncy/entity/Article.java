package com.laofan.strangetask.task.keywordFrequncy.entity;

import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * author:pan le
 * Date:2019/7/19
 * Time:9:45
 * @author sunwukong
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "article")
@ToString(
        exclude = {"id"})
@EntityListeners(value = AuditingEntityListener.class)
@EnableJpaAuditing
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "articleName",unique = true)
    private String name;

    @Column(name = "createTime")
    @Type(type = "java.time.LocalDateTime")
    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    @Type(type = "java.time.LocalDateTime")
    private LocalDateTime lastModifyTime;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User createUser;

}
