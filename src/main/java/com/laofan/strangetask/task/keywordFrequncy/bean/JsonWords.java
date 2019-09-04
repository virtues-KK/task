package com.laofan.strangetask.task.keywordFrequncy.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * author:pan le
 * Date:2019/9/2
 * Time:9:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonWords {
    @JsonProperty("Words")
    private List<Words_> words;
    @JsonProperty("Sentences")
    private List<Sentences_> sentences;
}
