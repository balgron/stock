package org.joder.stock.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * @author Joder 2020/8/25 18:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("best_param")
public class BestParam {

    @MongoId(targetType = FieldType.OBJECT_ID)
    private ObjectId id;
    private String tsCode;
    private String strategyCode;
    private Integer initMoney;
    private String startDate;
    private String endDate;
    private String params;
    private String bestParams;
    private Double profit;
    private Long createTime;
    private Integer state;
}
