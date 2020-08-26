package org.joder.stock.core.service.process.impl;

import org.joder.stock.core.StockStrategy;
import org.joder.stock.core.domain.StockProcess;
import org.joder.stock.core.domain.*;
import org.joder.stock.core.service.process.AbstractBackTestProcess;
import org.joder.stock.core.service.strategy.StockStrategyProcess;

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
        StockProcess process = stockStrategyProcess.buildProcess(query.getStockList(), query.getHyperParams());
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
        StockProcess process = stockStrategyProcess.buildProcess(query.getStockList(), query.getHyperParams());
        process.setProcessResult(processResult);
        TradeInfo info = new TradeInfo(query.getInitMoney());
        while (process.hasNext()) {
            process.next();
            info = stockStrategyProcess.doProcess(process);
            processResult.dealTrade(info);
        }
        return info;
    }

    private TradeInfo doLast(ProcessResult processResult, StockProcess process) {
        TradeInfo info = new TradeInfo(processResult.getMoney());
        if (processResult.getStockHoldState() == StockHoldState.HOLDING) {
            StockData last = process.getLast();
            info.setSale(true);
            info.setVolume(processResult.getVolume());
            info.setMoney(last.getClose());
            info.setDate(last.getDay());
        }
        return info;
    }
}
