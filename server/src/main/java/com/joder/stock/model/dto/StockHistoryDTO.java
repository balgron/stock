package com.joder.stock.model.dto;

import com.joder.stock.model.entity.StockHistory;
import com.joder.stock.util.StockUtil;
import lombok.Data;

/**
 * @author Joder 2020/8/19 21:57
 */
@Data
public class StockHistoryDTO {
    private String tsCode;
    private String tradeDate;
    private Double open;
    private Double close;
    private Double high;
    private Double low;
    private Double vol;

    public StockHistory toStockHistory() {
        StockHistory history = new StockHistory();
        history.setCode(StockUtil.convertCode(tsCode));
        history.setDay(tradeDate);
        history.setOpen(open);
        history.setClose(close);
        history.setHigh(high);
        history.setLow(low);
        history.setVolume(vol);
        return history;
    }
}
