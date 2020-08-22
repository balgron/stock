package com.joder.stock.strategy.service.strategy.ma;

import com.joder.stock.model.entity.StockHistory;
import com.joder.stock.strategy.domain.Process;
import com.joder.stock.strategy.domain.StockSuggest;
import com.joder.stock.strategy.service.strategy.AllInStockStrategyProcess;

/**
 * @author Joder 2020/8/16 16:14
 */
public class MeanStockStrategyProcessImpl implements AllInStockStrategyProcess {


    @Override
    public StockSuggest judgeSale(Process process) {
        int maxDayNum = getValue(process.getHyperParams(), "maxDayNum", 20);
        int minDayNum = getValue(process.getHyperParams(), "minDayNum", 10);
        double mean20 = mean(process, maxDayNum);
        double mean10 = mean(process, minDayNum);
        if (mean20 > 0 && mean10 > mean20) {
            return StockSuggest.BUY;
        }
        if (mean10 <= mean20) {
            return StockSuggest.SALE;
        }
        return StockSuggest.NOTHING;
    }

    private double mean(Process process, int dayNum) {
        double sum = 0;
        for (int i = 0; i < dayNum; i++) {
            StockHistory stock = process.getStock(process.getIndex() - i);
            if (stock == null) {
                return 0;
            }
            sum += stock.getClose();
        }
        return sum / dayNum;
    }
}
