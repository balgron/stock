package org.joder.stock.core.service.strategy.turtle.impl;

import org.joder.stock.core.domain.StockData;
import org.joder.stock.core.domain.StockProcess;
import org.joder.stock.core.domain.StockSuggest;
import org.joder.stock.core.service.strategy.AllInStockStrategyProcess;
import org.joder.stock.core.service.strategy.turtle.AbstractTurtleStockStrategyProcess;

/**
 * @author Joder 2020/8/14 21:05
 */
public class TurtleStockStrategyProcessImpl extends AbstractTurtleStockStrategyProcess implements AllInStockStrategyProcess {

    @Override
    public StockSuggest judgeSale(StockProcess process) {
        StockData currentStock = process.getCurrentStock();
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
