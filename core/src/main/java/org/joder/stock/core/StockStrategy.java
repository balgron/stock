package org.joder.stock.core;

import org.joder.stock.core.service.strategy.StockStrategyProcess;
import org.joder.stock.core.service.strategy.ma.MeanStockStrategyProcessImpl;
import org.joder.stock.core.service.strategy.macd.MacdStockStrategyProcessImpl;
import org.joder.stock.core.service.strategy.turtle.impl.TurtlePlusStockStrategyProcessImpl;
import org.joder.stock.core.service.strategy.turtle.impl.TurtleStockStrategyProcessImpl;

/**
 * 回测策略
 *
 * @author Joder 2020/8/14 20:42
 */
public enum StockStrategy {

    MEAN("mean", "均线策略", new MeanStockStrategyProcessImpl()),
    TURTLE("turtle", "海龟策略", new TurtleStockStrategyProcessImpl()),
    TURTLE_PLUS("turtle-plus", "海龟+策略", new TurtlePlusStockStrategyProcessImpl()),
    MACD("macd", "MACD策略", new MacdStockStrategyProcessImpl());

    private String code;
    private String name;
    private StockStrategyProcess stockStrategyProcess;

    StockStrategy(String code, String name, StockStrategyProcess stockStrategyProcess) {
        this.code = code;
        this.name = name;
        this.stockStrategyProcess = stockStrategyProcess;
    }

    public static StockStrategy parseStrategy(String code) {
        for (StockStrategy strategy : values()) {
            if (strategy.code.equals(code)) {
                return strategy;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public StockStrategyProcess getStockStrategyProcess() {
        return stockStrategyProcess;
    }
}
