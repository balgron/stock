package com.joder.stock.strategy.service.strategy;

import com.joder.stock.model.entity.StockHistory;
import com.joder.stock.strategy.domain.Process;
import com.joder.stock.strategy.domain.TradeInfo;

import java.util.List;
import java.util.Map;

/**
 * @author Joder 2020/8/14 20:44
 */
public interface StockStrategyProcess {

    /**
     * 根据当前条件，判断后续操作
     *
     * @param process 目前持有条件
     * @return 交易信息
     */
    TradeInfo doProcess(Process process);

    default Process buildProcess(List<StockHistory> stockList, Map<String, Object> hyperParams) {
        return new Process(stockList, hyperParams);
    }

    @SuppressWarnings("all")
    default <T> T getValue(Map<String, Object> map, String key, T defaultVal) {
        Object object = map.get(key);
        return object == null ? defaultVal : (T)object;
    }
}
