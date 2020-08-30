package org.joder.stock.model.dto;

import cn.hutool.core.date.DateUtil;
import lombok.Data;
import org.joder.stock.core.util.StockUtil;
import org.joder.stock.model.entity.StockHistory;

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
    private Double amount;

    public StockHistory toStockHistory() {
        StockHistory history = new StockHistory();
        history.setCode(StockUtil.convertCode(tsCode));
        history.setDay(DateUtil.formatDate(DateUtil.parse(tradeDate)));
        history.setOpen(open);
        history.setClose(close);
        history.setHigh(high);
        history.setLow(low);
        history.setVolume(vol);
        history.setAmount(amount);
        return history;
    }
}
