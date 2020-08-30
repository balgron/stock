package org.joder.stock.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * @author Joder 2020/8/8 21:24
 */
@Data
@Document("stock_list")
@CompoundIndex(name = "code_index", def = "{'code' : 1}")
public class Stock {

    @MongoId(FieldType.OBJECT_ID)
    @JsonIgnore
    private ObjectId id;
    @Field("ts_code")
    private String tsCode;
    private String symbol;
    private String name;
    @Field("list_date")
    private String listDate;
}
