package org.joder.stock.notify.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;

/**
 * @author Joder 2020/8/26 23:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockMessage {
    private String stockName;
    private Double currentPrice;
    private String strategyName;
    private String saleSuggest;
    private String profit;

    public static LinkedHashMap<String, String> getTitleMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("stockName", "股票名称");
        map.put("strategyName","策略名称");
        map.put("currentPrice","当前价格");
        map.put("saleSuggest","建议操作");
        map.put("profit", "过去两年该策略利润率");
        return map;
    }
}
