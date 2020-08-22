package com.joder.stock.model.dto;

import com.joder.stock.model.entity.Stock;
import com.joder.stock.util.StockUtil;
import lombok.Data;

/**
 * @author Joder 2020/8/19 21:57
 */
@Data
public class StockDTO {

    private String tsCode;
    private String symbol;
    private String name;

    public Stock toStock() {
        Stock stock = new Stock();
        stock.setTsCode(StockUtil.convertCode(tsCode));
        stock.setName(name);
        stock.setSymbol(symbol);
        return stock;
    }
}
