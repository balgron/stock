package org.joder.stock.model.dto;

import cn.hutool.core.date.DateUtil;
import lombok.Data;
import org.joder.stock.core.util.StockUtil;
import org.joder.stock.model.entity.Stock;

/**
 * @author Joder 2020/8/19 21:57
 */
@Data
public class StockDTO {

    private String tsCode;
    private String symbol;
    private String name;
    private String listStatus;
    private String listDate;

    public Stock toStock() {
        Stock stock = new Stock();
        stock.setTsCode(StockUtil.convertCode(tsCode));
        stock.setName(name);
        stock.setSymbol(symbol);
        stock.setListDate(DateUtil.formatDate(DateUtil.parse(listDate)));
        return stock;
    }
}
