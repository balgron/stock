package com.joder.stock.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * 股票策略
 *
 * @author Joder 2020/8/22 15:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("stock_strategy")
public class StockStrategy {

    @JsonIgnore
    @MongoId(FieldType.OBJECT_ID)
    private ObjectId id;
    private String uuid;
    private Double initMoney;
    private Long createTime;
    private String endDate;
    private String strategyCode;
    private String params;
    /**
     * 运行状态: 运行（0）、结束（1）、失败(-1)
     */
    private Integer state;

}
