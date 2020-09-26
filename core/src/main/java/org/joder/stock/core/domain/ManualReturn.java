package org.joder.stock.core.domain;

import lombok.Data;

/**
 * @author Joder 2020/9/13 11:24
 */
@Data
public class ManualReturn {

    private TradeReturn machine;
    /**
     * 初始化金额
     */
    private double initMoney;
    /**
     * 最大购买金额
     */
    private double holdMoney;
    private int holdVolume;
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

    private StockSuggest stockSuggest;

    private double tax;

}
