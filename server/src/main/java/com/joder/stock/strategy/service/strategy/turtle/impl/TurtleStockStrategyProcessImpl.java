package com.joder.stock.strategy.service.strategy.turtle.impl;

import com.joder.stock.model.entity.StockHistory;
import com.joder.stock.strategy.domain.Process;
import com.joder.stock.strategy.domain.StockSuggest;
import com.joder.stock.strategy.service.strategy.AllInStockStrategyProcess;
import com.joder.stock.strategy.service.strategy.turtle.AbstractTurtleStockStrategyProcess;

/**
 * @author Joder 2020/8/14 21:05
 */
public class TurtleStockStrategyProcessImpl extends AbstractTurtleStockStrategyProcess implements AllInStockStrategyProcess {

    @Override
    public StockSuggest judgeSale(Process process) {
        StockHistory currentStock = process.getCurrentStock();
        int index = process.getIndex();
        int maxDayNum = getValue(process.getHyperParams(), "maxDayNum", 10);
        int minDayNum = getValue(process.getHyperParams(), "minDayNum", 5);
        double max20 = finMax(process, index - 1, maxDayNum);
        if (currentStock.getClose() > max20) {
            return StockSuggest.BUY;
        }
        double min10 = findMin(process, index - 1, minDayNum);
        if (currentStock.getClose() < min10) {
            return StockSuggest.SALE;
        }
        return StockSuggest.NOTHING;
    }

}
