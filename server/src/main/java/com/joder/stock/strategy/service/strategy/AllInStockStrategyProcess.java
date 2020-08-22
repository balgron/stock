package com.joder.stock.strategy.service.strategy;

import com.joder.stock.model.entity.StockHistory;
import com.joder.stock.strategy.domain.Process;
import com.joder.stock.strategy.domain.*;
import com.joder.stock.util.StockUtil;

/**
 * 该类作用是，手里掌握的资金在买时all in, 在卖时全部股票卖出
 * @author Joder 2020/8/16 17:00
 */
public interface AllInStockStrategyProcess extends StockStrategyProcess {

    @Override
    default TradeInfo doProcess(Process process) {
        StockHistory stock = process.getCurrentStock();
        ProcessResult processResult = process.getProcessResult();
        TradeInfo info = new TradeInfo(processResult.getMoney());
        info.setDate(stock.getDay());
        StockSuggest stockSuggest = judgeSale(process);
        double close = stock.getClose();
        if (stockSuggest == StockSuggest.SALE && processResult.getStockHoldState() == StockHoldState.HOLDING) {
            info.setMoney(close);
            info.setVolume(processResult.getVolume());
            info.setSale(true);
        } else if (stockSuggest == StockSuggest.BUY) {
            int num = StockUtil.canBuy(info.getHoldMoney(), close);
            info.setSale(false);
            info.setMoney(close);
            info.setVolume(num);
        }
        return info;
    }

    StockSuggest judgeSale(Process process);
}
