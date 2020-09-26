package org.joder.stock.core.domain;

import lombok.Data;

import java.util.List;

/**
 * 模拟回测
 *
 * @author Joder 2020/9/13 10:32
 */
@Data
public class ManualQuery extends ProcessQuery {

    private double holdMoney;
    private int holdVolume;

    private String tsCode;
    private String date;
    private double originMoney;
    private double price;
    private int volume;
    private StockSuggest stockSuggest;

    public ManualQuery(String strategyCode, List<StockData> stockList, Double holdMoney, Integer holdVolume,
                       String tsCode, String date, Double originMoney, Double price, Integer volume, StockSuggest stockSuggest) {
        super(strategyCode, stockList);
        this.holdMoney = holdMoney;
        this.holdVolume = holdVolume;
        this.tsCode = tsCode;
        this.date = date;
        this.originMoney = originMoney;
        this.price = price;
        this.volume = volume;
        this.stockSuggest = stockSuggest;
    }
}
