package com.joder.stock.model.vo;

import lombok.Data;

/**
 * @author Joder 2020/8/22 23:14
 */
@Data
public class StockStrategyResultVO {

    private String uuid;
    private String tsCode;
    private Double total;
    private Double one;
    private Double two;
    private Double three;
}
