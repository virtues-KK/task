package com.laofan.strangetask.task.keywordFrequncy.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.sound.midi.MidiFileFormat;

@Entity
@ToString(exclude = {"id"})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtcleKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long articleId;

    private Long keywordId;

    /**
     * tf-idf值
     */
    private Double tf_idf;
}
