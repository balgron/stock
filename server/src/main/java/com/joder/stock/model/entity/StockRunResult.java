package com.joder.stock.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * 存放股票回测时的结果
 *
 * @author Joder 2020/8/22 15:48
 */
@Data
@Builder
@Document("stock_run_result")
public class StockRunResult {

    @JsonIgnore
    @MongoId(targetType = FieldType.OBJECT_ID)
    private ObjectId id;
    /**
     * 股票code
     */
    @Field("ts_code")
    private String tsCode;
    @Field("start_date")
    private String startDate;
    @Field("end_date")
    private String endDate;
    /**
     * 对应stock_strategy表的uuid
     */
    @Field("strategy_uuid")
    private String strategyUuid;
    /**
     * 利润率
     */
    private Double profit;
    /**
     * 总交易次数
     */
    @Field("trade_num")
    private Integer tradeNum;
    /**
     * 平均每笔回报利润率
     */
    @Field("mean_profit")
    private Double meanProfit;
    /**
     * 最大获利
     */
    @Field("max_profit")
    private Double maxProfit;
    /**
     * 最大亏损
     */
    @Field("max_loss")
    private Double maxLoss;
    /**
     * 盈亏比
     */
    @Field("profit_loss_rate")
    private Double profitLossRate;
    /**
     * 盈利笔数
     */
    @Field("profit_num")
    private Integer profitNum;
    /**
     * 亏损笔数
     */
    @Field("loss_num")
    private Integer lossNum;
    /**
     * 盈利比例
     */
    @Field("profit_rate")
    private Double profitRate;
    /**
     * 最大连续亏损笔数
     */
    @Field("max_loss_num")
    private Integer maxLossNum;
    /**
     * 最大连续亏损金额
     */
    @Field("max_loss_money")
    private Double maxLossMoney;
}
