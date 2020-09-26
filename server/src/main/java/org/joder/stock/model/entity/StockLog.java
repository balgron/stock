package org.joder.stock.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * @author Joder 2020/9/23 22:55
 */
@Data
@Document("stock_log")
public class StockLog {

    @MongoId(targetType = FieldType.OBJECT_ID)
    @JsonIgnore
    private ObjectId id;
    private String day;
    private Long time;

    private String[] tags;

    /**
     * 感觉
     */
    private String feel;
    /**
     * 市场
     */
    private String market;
    /**
     * 内容
     */
    private String content;
}
