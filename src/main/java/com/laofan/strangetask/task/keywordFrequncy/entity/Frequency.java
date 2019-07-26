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
 * @author sunwukong
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Frequency {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "keywordId")
    private Keyword keyword;

    @ManyToOne
    @JoinColumn(name = "articleId")
    private Article article;

    private Long frequency;

}
