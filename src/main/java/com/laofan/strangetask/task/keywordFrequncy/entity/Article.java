package com.laofan.strangetask.task.keywordFrequncy.entity;

import lombok.*;

import javax.persistence.*;
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
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "articleName")
    private String name;

    @Column(name = "createTime")
    private String createDate;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User createUser;

    @ManyToMany(mappedBy = "articles")
    private List<Frequency> frequencies;

}
