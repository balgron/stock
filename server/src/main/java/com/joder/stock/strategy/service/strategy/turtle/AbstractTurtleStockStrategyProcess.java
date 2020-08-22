package com.joder.stock.strategy.service.strategy.turtle;

import com.joder.stock.model.entity.StockHistory;
import com.joder.stock.strategy.domain.Process;
import com.joder.stock.strategy.domain.StockHoldState;
import com.joder.stock.strategy.service.strategy.StockStrategyProcess;

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
    protected double findMin(Process process, int index, int dayNum) {
        return find(process, index, dayNum, Double.MAX_VALUE, Math::min);
    }

    protected double finMax(Process process, int index, int dayNum) {
        return find(process, index, dayNum, Double.MIN_VALUE, Math::max);
    }

    private double find(Process process, int index, int dayNum, double defultValue, ToDoubleBiFunction<Double, Double> func) {
        double ret = defultValue;
        for (int i = 0; i < dayNum; i++) {
            StockHistory stock = process.getStock(index - i);
            if (stock == null) {
                return ret;
            } else {
                ret = func.applyAsDouble(ret, stock.getClose());
            }
        }
        return ret;
    }

}
