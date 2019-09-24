package com.laofan.strangetask.task.keywordFrequncy.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

/**
 * author:pan le
 * Date:2019/9/24
 * Time:15:45
 */
@Entity
@Table(name = "convertFailed")
@Builder
@Data
public class ConvertFailedEntity {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    private String FileName;
}
