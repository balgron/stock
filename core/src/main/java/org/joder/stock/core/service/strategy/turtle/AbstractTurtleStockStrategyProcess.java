package org.joder.stock.core.service.strategy.turtle;


import org.joder.stock.core.domain.StockData;
import org.joder.stock.core.domain.StockProcess;
import org.joder.stock.core.service.strategy.StockStrategyProcess;

import java.util.function.ToDoubleBiFunction;

/**
 * @author Joder 2020/8/15 15:01
 */
public abstract class AbstractTurtleStockStrategyProcess implements StockStrategyProcess {

    /**
     * 最近的最低点
     *
     * @param process 条件
     * @param dayNum  天数
     * @return dayNum日最低点
     */
    protected double findMin(StockProcess process, int index, int dayNum) {
        return find(process, index, dayNum, Double.MAX_VALUE, Math::min);
    }

    protected double finMax(StockProcess process, int index, int dayNum) {
        return find(process, index, dayNum, Double.MIN_VALUE, Math::max);
    }

    private double find(StockProcess process, int index, int dayNum, double defultValue, ToDoubleBiFunction<Double, Double> func) {
        double ret = defultValue;
        for (int i = 0; i < dayNum; i++) {
            StockData stock = process.getStock(index - i);
            if (stock == null) {
                return ret;
            } else {
                ret = func.applyAsDouble(ret, stock.getClose());
            }
        }
        return ret;
    }

}
