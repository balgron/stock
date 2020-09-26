package org.joder.stock.model.query;

import lombok.Data;

/**
 * @author Joder 2020/9/13 19:45
 */
@Data
public class StockSimulationQuery extends BackTestQuery {

    private Double holdMoney;
    private Integer holdVolume;
    private String date;
    private Double originMoney;
    private Double price;
    private Integer volume;
    private String operate;
}
