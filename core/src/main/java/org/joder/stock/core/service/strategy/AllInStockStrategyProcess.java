package org.joder.stock.core.service.strategy;

import org.joder.stock.core.domain.*;
import org.joder.stock.core.util.StockUtil;

/**
 * 该类作用是，手里掌握的资金在买时all in, 在卖时全部股票卖出
 *
 * @author Joder 2020/8/16 17:00
 */
public interface AllInStockStrategyProcess extends StockStrategyProcess {

    @Override
    default TradeInfo doProcess(StockProcess process) {
        StockData stock = process.getCurrentStock();
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

    StockSuggest judgeSale(StockProcess process);
}
