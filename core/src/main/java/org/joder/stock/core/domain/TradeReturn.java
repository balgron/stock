package org.joder.stock.core.domain;

import lombok.Data;

/**
 * @author Joder 2020/8/29 21:54
 */
@Data
public class TradeReturn {
    private String strategyCode;
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

    public TradeReturn(String strategyCode, double holdMoney) {
        this.holdMoney = holdMoney;
    }

    public TradeReturn(String strategyCode, TradeInfo info) {
        this.strategyCode = strategyCode;
        holdMoney = info.getHoldMoney();
        date = info.getDate();
        volume = info.getVolume();
        money = info.getMoney();
        originMoney = info.getOriginMoney();
        sale = info.isSale();
        tax = info.getTax();
    }
}
