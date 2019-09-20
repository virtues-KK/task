package com.laofan.strangetask.task.keywordFrequncy.entity;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * author:pan le
 * Date:2019/9/17
 * Time:11:31
 */
@Document(indexName = "content",type = "article")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EsArticleDocument {

    private Long id;

    private String articleName;

    private String articleContent;

    /**
     * 内容推荐关键字
     */
    private String keyWord;

}
