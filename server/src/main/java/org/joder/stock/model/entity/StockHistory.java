package org.joder.stock.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * @author Joder 2020/8/8 21:21
 */
@Data
@Document("stock_history")
public class StockHistory {

    @MongoId(FieldType.OBJECT_ID)
    @JsonIgnore
    private ObjectId id;
    private String code;
    private String day;
    private Double open;
    private Double close;
    private Double high;
    private Double low;
    private Double volume;
}
