package com.laofan.strangetask.task.keywordFrequncy.entity;

import lombok.ToString;

import javax.persistence.*;
import java.util.List;

/**
 * author:pan le
 * Date:2019/7/19
 * Time:9:47
 * @author sunwukong
 */
@Entity
@ToString(
        exclude = "articles"
)
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String username;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "createUser")
    private List<Article> articles;
}
