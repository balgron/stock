package org.joder.stock.core.domain;

import lombok.Data;

/**
 * @author Joder 2020/8/8 21:21
 */
@Data
public class StockData {

    private String day;
    private Double open;
    private Double close;
    private Double high;
    private Double low;
    private Double volume;

    public  StockData(String day, Double open, Double close, Double high,Double low,Double volume) {
        this.day = day;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.volume = volume;
    }
}
