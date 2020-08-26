package org.joder.stock.core.service.strategy;

import org.joder.stock.core.domain.*;
import org.joder.stock.core.domain.StockProcess;

import java.util.List;
import java.util.Map;

/**
 * 根据每天掌握的信息，判断今天的交易情况
 *
 * @author Joder 2020/8/14 20:44
 */
public interface StockStrategyProcess {

    /**
     * 根据当前条件，判断后续操作
     *
     * @param process 目前持有条件
     * @return 交易信息, tradeInfo.money=0,代表今天不交易
     */
    TradeInfo doProcess(StockProcess process);

    default StockProcess buildProcess(List<StockData> stockList, Map<String, Object> hyperParams) {
        return new StockProcess(stockList, hyperParams);
    }

    @SuppressWarnings("all")
    default <T> T getValue(Map<String, Object> map, String key, T defaultVal) {
        Object object = map.get(key);
        return object == null ? defaultVal : (T) object;
    }
}
