package com.joder.stock.request.domain;

/**
 * @author Joder 2020/8/19 21:12
 */
public enum StockApi {
    DAILY("daily"),
    STOCK_BASIC("stock_basic");

    private String name;

    StockApi(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
