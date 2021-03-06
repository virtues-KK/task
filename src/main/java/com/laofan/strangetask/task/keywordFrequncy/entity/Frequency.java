package com.laofan.strangetask.task.keywordFrequncy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

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
@Data
public class Frequency {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keywordId")
    private Keyword keyword;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articleId")
    private Article article;

    private Long frequency;

    @Column(columnDefinition = "DOUBLE default 0.00")
    private double tf_idfValue = 0.00;

}
