package org.joder.stock.core.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Joder 2020/8/14 21:36
 */
@Data
public class ProcessQuery {
    private double initMoney;
    private String strategyCode;
    private List<StockData> stockList;
    /**
     * 对应每个策略需要的参数
     */
    private Map<String, Object> hyperParams;

    public ProcessQuery(String strategyCode, List<StockData> stockList) {
        this(100000, strategyCode, stockList);
    }

    public ProcessQuery(double initMoney, String strategyCode, List<StockData> stockList) {
        this(initMoney, strategyCode, stockList, null);
    }

    public ProcessQuery(double initMoney, String strategyCode, List<StockData> stockList, Map<String, Object> hyperParams) {
        super();
        this.initMoney = initMoney;
        this.strategyCode = strategyCode;
        this.stockList = stockList;
        this.hyperParams = hyperParams;
    }
}
