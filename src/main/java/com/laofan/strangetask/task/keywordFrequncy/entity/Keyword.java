package com.laofan.strangetask.task.keywordFrequncy.entity;

import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * author:pan le
 * Date:2019/7/24
 * Time:9:57
 */
@Entity
@EntityListeners(value = AuditingEntityListener.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"id"})
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "keyword")
    private String name;

    @Column(name = "createTime")
    @Type(type = "java.time.LocalDateTime")
    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    @Type(type = "java.time.LocalDateTime")
    private LocalDateTime lastModifyTime;
}
