package org.joder.stock.model.entity;

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
 * @author Joder 2020/8/29 16:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("stock_real")
public class StockReal {

    @MongoId(targetType = FieldType.OBJECT_ID)
    @JsonIgnore
    private ObjectId id;
    private String tsCode;
    private Long time;
    private Double currentPrice;
    private Double open;
    private Double high;
    private Double low;
    private Double volume;
    private Double amount;

    private Double buyNum1;
    private Double buyPrice1;
    private Double buyNum2;
    private Double buyPrice2;
    private Double buyNum3;
    private Double buyPrice3;
    private Double buyNum4;
    private Double buyPrice4;
    private Double buyNum5;
    private Double buyPrice5;

    private Double saleNum1;
    private Double salePrice1;
    private Double saleNum2;
    private Double salePrice2;
    private Double saleNum3;
    private Double salePrice3;
    private Double saleNum4;
    private Double salePrice4;
    private Double saleNum5;
    private Double salePrice5;
}
