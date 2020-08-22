package com.joder.stock.strategy.domain;

import lombok.Data;

/**
 * @author Joder 2020/8/14 23:46
 */
@Data
public class TradeInfo {
    /**
     * 最大购买金额
     */
    private double holdMoney;
    /**
     * 交易日期
     */
    private String date;
    /**
     * 交易量
     */
    private int volume;
    /**
     * 每股价格
     */
    private double money;
    /**
     * 每手持有价格
     */
    private double originMoney;
    /**
     * 是否出售 true:卖出; false:买进
     */
    private boolean sale;

    private double tax;

    public TradeInfo(double holdMoney) {
        this.holdMoney = holdMoney;
    }
}
