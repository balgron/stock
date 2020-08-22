package com.joder.stock.strategy.domain;

/**
 * @author Joder 2020/8/14 20:47
 */
public enum StockHoldState {
    /**
     * 持有
     */
    HOLDING,
    /**
     * 未持有
     */
    UN_HOLD;

    public static StockHoldState valueOf(double volume) {
        if (volume > 0) {
            return HOLDING;
        }
        return UN_HOLD;
    }
}
