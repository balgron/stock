package org.joder.stock.core.service.strategy.turtle.impl;

import org.joder.stock.core.domain.StockProcess;
import org.joder.stock.core.domain.*;
import org.joder.stock.core.service.strategy.turtle.AbstractTurtleStockStrategyProcess;
import org.joder.stock.core.util.StockUtil;
import org.springframework.util.Assert;

/**
 * @author Joder 2020/8/14 21:05
 */
public class TurtlePlusStockStrategyProcessImpl extends AbstractTurtleStockStrategyProcess {

    @Override
    public TradeInfo doProcess(StockProcess process) {
        StockData stock = process.getCurrentStock();
        ProcessResult processResult = process.getProcessResult();
        TradeInfo info = new TradeInfo(processResult.getMoney());
        info.setDate(stock.getDay());
        if (!process.containContext("TOTAL_MONEY")) {
            process.setContext("TOTAL_MONEY", processResult.getInitMoney());
        }
        StockSuggest stockSuggest = judgeSale(process);
        if (stockSuggest == StockSuggest.SALE && processResult.getStockHoldState() == StockHoldState.HOLDING) {
            sale(process, processResult, info);
        } else if (stockSuggest == StockSuggest.BUY) {
            buy(process, info);
        }
        return info;
    }

    private StockSuggest judgeSale(StockProcess process) {
        atr(process);
        Double pdAtr = process.getContext("PD_ATR", Double.TYPE);
        Double atr = process.getContext("ATR", Double.TYPE);
        if (pdAtr == null || atr == null) {
            return StockSuggest.NOTHING;
        }
        StockData currentStock = process.getCurrentStock();
        int index = process.getIndex();
        int maxDayNum = getValue(process.getHyperParams(), "maxDayNum", 10);
        int minDayNum = getValue(process.getHyperParams(), "minDayNum", 5);
        double max20 = finMax(process, index - 1, maxDayNum);
        if (currentStock.getClose() > max20) {
            if (process.getProcessResult().getStockHoldState() == StockHoldState.UN_HOLD) {
                return StockSuggest.BUY;
            } else if (fill(process)) {
                return StockSuggest.NOTHING;
            }
            return increase(process) ? StockSuggest.BUY : StockSuggest.NOTHING;
        }
        double min10 = findMin(process, index - 1, minDayNum);
        if (currentStock.getClose() < min10) {
            return StockSuggest.SALE;
        }
        if (decrease(process)) {
            return StockSuggest.SALE;
        }
        return StockSuggest.NOTHING;
    }

    private boolean decrease(StockProcess process) {
        if (process.getProcessResult().getStockHoldState() != StockHoldState.HOLDING) {
            return false;
        }
        // 减仓atr幅度
        double decreaseRange = getValue(process.getHyperParams(), "decreaseRange", 2);
        Double baseAtr = process.getContext("PD_ATR", Double.TYPE);
        StockData yesterday = process.getStock(process.getIndex() - 1);
        if (yesterday == null || baseAtr == null) {
            return false;
        }
        ProcessResult processResult = process.getProcessResult();
        double total = process.getContext("TOTAL_MONEY", Double.TYPE);
        double money = process.getCurrentStock().getClose() * processResult.getVolume();
        return total - money >= total * decreaseRange * 0.01;
    }

    private boolean increase(StockProcess process) {
        // 加仓atr幅度
        double increaseRange = getValue(process.getHyperParams(), "increaseRange", 0.5);
        Double baseAtr = process.getContext("PD_ATR", Double.TYPE);
        ProcessResult processResult = process.getProcessResult();
        if (baseAtr == null || processResult.getStockHoldState() == StockHoldState.UN_HOLD) {
            return false;
        }
        double baseMoney = processResult.getStockValue() / processResult.getVolume();
        double currentClose = process.getCurrentStock().getClose();
        return currentClose >= baseMoney + increaseRange * hasMulti(process) * 0.01;
    }

    private double hasMulti(StockProcess process) {
        Integer baseNum = process.getContext("BASE_NUM", Integer.TYPE);
        return Math.floor(process.getProcessResult().getVolume() / baseNum);
    }

    private boolean fill(StockProcess process) {
        // 每次最多持有初始购买倍数
        int maxUnit = getValue(process.getHyperParams(), "maxUnit", 4);
        Integer baseNum = process.getContext("BASE_NUM", Integer.TYPE);
        return baseNum != null && process.getProcessResult().getVolume() >= baseNum * maxUnit;
    }

    private void sale(StockProcess process, ProcessResult processResult, TradeInfo info) {
        StockData stock = process.getCurrentStock();
        double close = stock.getClose();
        process.removeContext("BASE_NUM");
        // 重新设置总金额
        process.setContext("TOTAL_MONEY", processResult.getMoney() + (close * processResult.getVolume()));
        info.setMoney(close);
        info.setVolume(processResult.getVolume());
        info.setSale(true);
    }

    private void buy(StockProcess process, TradeInfo info) {
        // 可使用资金比例
        double moneyRate = getValue(process.getHyperParams(), "moneyRate", 0.01);
        double atr = process.getContext("ATR", Double.TYPE);
        StockData stock = process.getCurrentStock();
        double close = stock.getClose();
        // 持有的资金最多购买数量
        int num = StockUtil.canBuy(info.getHoldMoney(), close);
        // 根据atr, 计算每次最多买进数量
        int max;
        Integer baseNum = process.getContext("BASE_NUM", Integer.TYPE);
        double totalMoney = process.getContext("TOTAL_MONEY", Double.TYPE);
        if (baseNum == null) {
            max = ((int) ((totalMoney * moneyRate) / (atr * StockUtil.MIN_BASE))) * StockUtil.MIN_BASE;
            process.setContext("BASE_NUM", max);
            process.setContext("BASE_MONEY", close);
        } else {
            max = baseNum;
        }
        info.setSale(false);
        info.setMoney(close);
        info.setVolume(Math.min(num, max));
    }

    private double tr(StockProcess process, int index) {
        StockData currentStock = process.getStock(index);
        StockData yesterday = process.getStock(index - 1);
        if (yesterday == null) {
            return 0;
        }
        double hl = currentStock.getHigh() - currentStock.getLow();
        double hpc = Math.abs(currentStock.getHigh() - yesterday.getClose());
        double pcl = Math.abs(yesterday.getClose() - currentStock.getLow());
        return Math.max(hl, Math.max(hpc, pcl));
    }

    private void atr(StockProcess process) {
        int atrLength = getValue(process.getHyperParams(), "atrLength", 10);
        Assert.isTrue(atrLength > 1, "atrLength必须大于1");
        int index = process.getIndex();
        if (atrLength - 1 == index) {
            double meanTr = meanTr(process, index, atrLength);
            process.setContext("ATR", meanTr);
            process.setContext("PD_ATR", 0.0);
        } else if (atrLength - 1 < index) {
            double pdAtr = process.getContext("ATR", Double.TYPE);
            process.setContext("PD_ATR", pdAtr);
            double atr = ((atrLength - 1) * pdAtr + tr(process, index)) / atrLength;
            process.setContext("ATR", atr);
        }
    }

    private double meanTr(StockProcess process, int index, int atrLength) {
        double sum = 0;
        for (int i = 0; i < atrLength - 1; i++) {
            StockData stock = process.getStock(index);
            sum += tr(process, index - i);
        }
        return sum / (atrLength - 1);
    }
}
