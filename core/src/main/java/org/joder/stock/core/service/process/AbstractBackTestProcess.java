package org.joder.stock.core.service.process;

import org.joder.stock.core.StockStrategy;

/**
 * @author Joder 2020/8/15 15:48
 */
public abstract class AbstractBackTestProcess implements BackTestProcess {

    protected StockStrategy getStrategy(String code) {
        return StockStrategy.parseStrategy(code);
    }
}
