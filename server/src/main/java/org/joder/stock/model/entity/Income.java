package org.joder.stock.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document("income")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Income {
    @MongoId(targetType = FieldType.OBJECT_ID)
    private ObjectId id;
    private String date;
    private Long time;
    private Double money;
    private Byte type;
    private Integer tags;
    private String description;
}
