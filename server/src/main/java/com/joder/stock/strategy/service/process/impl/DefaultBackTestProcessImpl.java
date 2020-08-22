package com.joder.stock.strategy.service.process.impl;

import com.joder.stock.model.entity.StockHistory;
import com.joder.stock.strategy.StockStrategy;
import com.joder.stock.strategy.domain.Process;
import com.joder.stock.strategy.domain.*;
import com.joder.stock.strategy.service.process.AbstractBackTestProcess;
import com.joder.stock.strategy.service.strategy.StockStrategyProcess;

/**
 * @author Joder 2020/8/14 21:38
 */
public class DefaultBackTestProcessImpl extends AbstractBackTestProcess {

    @Override
    public ProcessResult doProcess(ProcessQuery query) {
        StockStrategy strategy = getStrategy(query.getStrategyCode());
        StockStrategyProcess stockStrategyProcess = strategy.getStockStrategyProcess();
        Process process = stockStrategyProcess.buildProcess(query.getStockList(), query.getHyperParams());
        ProcessResult processResult = new ProcessResult(query.getInitMoney());
        process.setProcessResult(processResult);
        while (process.hasNext()) {
            process.next();
            processResult.dealTrade(stockStrategyProcess.doProcess(process));
        }
        processResult.dealTrade(doLast(processResult, process));
        return processResult;
    }

    private TradeInfo doLast(ProcessResult processResult, Process process) {
        TradeInfo info = new TradeInfo(processResult.getMoney());
        if (processResult.getStockHoldState() == StockHoldState.HOLDING) {
            StockHistory last = process.getLast();
            info.setSale(true);
            info.setVolume(processResult.getVolume());
            info.setMoney(last.getClose());
            info.setDate(last.getDay());
        }
        return info;
    }
}
