package com.joder.stock.model.query;

import lombok.Data;

import java.util.Map;

/**
 * @author Joder 2020/8/16 9:31
 */
@Data
public class BackTestQuery {

    /**
     * 初始金额
     */
    private double initMoney;
    /**
     * 策略
     */
    private String strategyCode;
    /**
     * 附加参数
     */
    private Map<String, Object> hyperParams;
    /**
     * 股票代号
     */
    private String stockCode;
    /**
     * 回测开始时间
     */
    private String startDate;
    /**
     * 回测结束时间
     */
    private String endDate;
}
