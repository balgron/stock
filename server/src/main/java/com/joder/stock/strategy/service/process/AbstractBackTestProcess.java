package com.joder.stock.strategy.service.process;

import com.joder.stock.strategy.StockStrategy;

/**
 * @author Joder 2020/8/15 15:48
 */
public abstract class AbstractBackTestProcess implements BackTestProcess {

    protected StockStrategy getStrategy(String code) {
        return StockStrategy.parseStrategy(code);
    }
}
