package com.laofan.strangetask.task.keywordFrequncy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * author:pan le
 * Date:2019/9/10
 * Time:15:43
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@NamedNativeQuery(name ="AliyunParameterEntity.findMinByTime",query = "SELECT parameter,remain_time from parameter where parameter.remain_time > ?1 ORDER BY remain_time LIMIT 1")
@Table(name = "parameter",uniqueConstraints = {@UniqueConstraint(columnNames = "parameter")})
public class AliyunParameterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false, length = 200)
    private String parameter;

    /**
     * 账号剩余时间
     * second
     */
    private Long remainTime;

    public static AliyunParameterEntity gennerate(String parameter){
        return new AliyunParameterEntity(null,parameter,7200L);
    }
}
