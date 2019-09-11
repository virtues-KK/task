package com.laofan.strangetask.task.keywordFrequncy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * author:pan le
 * Date:2019/9/11
 * Time:10:35
 * @author sunwukong
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@NamedNativeQuery(name="findByRetryFile",query = "SELECT file_name from retry where file_name = ?1")
@Table(name = "retry")
public class RehandleFiles {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(unique = true,length = 200,name= "fileName")
    private String retryFile;

    public static RehandleFiles buildEntity(String reTryFile){
        return new RehandleFiles(null,reTryFile);
    }
}
