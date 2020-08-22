package com.joder.stock.strategy.service.process.impl;

import com.joder.stock.model.entity.StockHistory;
import com.joder.stock.strategy.StockStrategy;
import com.joder.stock.strategy.domain.Process;
import com.joder.stock.strategy.domain.*;
import com.joder.stock.strategy.service.process.AbstractBackTestProcess;
import com.joder.stock.strategy.service.strategy.StockStrategyProcess;

/**
 * 默认对策略反馈的结果进行汇总处理
 *
 * @author Joder 2020/8/14 21:38
 */
public class DefaultBackTestProcessImpl extends AbstractBackTestProcess {

    @Override
    public ProcessResult doProcess(ProcessQuery query) {
        ProcessResult processResult = new ProcessResult(query.getInitMoney());
        if (query.getStockList().isEmpty()) {
            return processResult;
        }
        StockStrategy strategy = getStrategy(query.getStrategyCode());
        StockStrategyProcess stockStrategyProcess = strategy.getStockStrategyProcess();
        Process process = stockStrategyProcess.buildProcess(query.getStockList(), query.getHyperParams());
        process.setProcessResult(processResult);
        while (process.hasNext()) {
            process.next();
            processResult.dealTrade(stockStrategyProcess.doProcess(process));
        }
        processResult.dealTrade(doLast(processResult, process));
        return processResult;
    }

    @Override
    public TradeInfo predictLast(ProcessQuery query) {
        ProcessResult processResult = new ProcessResult(query.getInitMoney());
        if (query.getStockList().isEmpty()) {
            return new TradeInfo(query.getInitMoney());
        }
        StockStrategy strategy = getStrategy(query.getStrategyCode());
        StockStrategyProcess stockStrategyProcess = strategy.getStockStrategyProcess();
        Process process = stockStrategyProcess.buildProcess(query.getStockList(), query.getHyperParams());
        process.setProcessResult(processResult);
        TradeInfo info = new TradeInfo(query.getInitMoney());
        while (process.hasNext()) {
            process.next();
            info = stockStrategyProcess.doProcess(process);
            processResult.dealTrade(info);
        }
        return info;
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
