package org.joder.stock.core.service.process.impl;

import org.joder.stock.core.StockStrategy;
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
    public TradeReturn predictLast(ProcessQuery query) {
        ProcessResult processResult = new ProcessResult(query.getInitMoney());
        if (query.getStockList().isEmpty()) {
            return new TradeReturn(query.getStrategyCode(), query.getInitMoney());
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
        return new TradeReturn(query.getStrategyCode(), info);
    }

    @Override
    public ManualReturn simulation(ManualQuery query) {
        ManualReturn ret = new ManualReturn();
        TradeReturn tradeReturn = predictLast(query);
        ret.setMachine(tradeReturn);
        dealManual(ret, query);
        return ret;
    }

    private void dealManual(ManualReturn ret, ManualQuery query) {
        ProcessResult processResult = new ProcessResult(query.getInitMoney());
        processResult.setMoney(query.getHoldMoney());
        processResult.setStockHoldState(query.getHoldVolume() > 0 ? StockHoldState.HOLDING : StockHoldState.UN_HOLD);
        processResult.setVolume(query.getHoldVolume());
        processResult.setStockValue(query.getOriginMoney() * query.getHoldVolume());
        TradeInfo info = new TradeInfo();
        info.setDate(query.getDate());
        info.setHoldMoney(query.getHoldMoney());
        info.setMoney(query.getPrice());
        info.setOriginMoney(query.getOriginMoney());
        info.setVolume(query.getVolume());
        info.setSale(query.getStockSuggest() == StockSuggest.SALE);
        processResult.dealTrade(info);
        ret.setDate(query.getDate());
        ret.setHoldMoney(processResult.getMoney());
        ret.setHoldVolume(query.getHoldVolume() + (query.getStockSuggest() == StockSuggest.SALE ? -1 : 1) * query.getVolume());
        ret.setInitMoney(query.getInitMoney());
        ret.setStockSuggest(query.getStockSuggest());
        ret.setMoney(query.getPrice());
        ret.setVolume(processResult.getVolume());
        ret.setTax(info.getTax());
        ret.setOriginMoney(processResult.getVolume() == 0 ? 0 : processResult.getStockValue() / processResult.getVolume());
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
