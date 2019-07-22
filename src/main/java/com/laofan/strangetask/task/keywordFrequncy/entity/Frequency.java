package com.laofan.strangetask.task.keywordFrequncy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * author:pan le
 * Date:2019/7/19
 * Time:10:05
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Frequency {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String keyword;

    private Long frequency;

    @Column(name = "articleId")
    @ManyToMany
    private List<Article> articles;

}
