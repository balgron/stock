package com.joder.stock.service;

import cn.hutool.core.io.resource.ResourceUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.joder.stock.model.entity.StockHistory;
import com.joder.stock.model.query.BackTestQuery;
import com.joder.stock.model.vo.BackTestResultVO;
import com.joder.stock.repository.StockHistoryRepository;
import com.joder.stock.strategy.StockStrategy;
import com.joder.stock.strategy.domain.ProcessQuery;
import com.joder.stock.strategy.domain.ProcessResult;
import com.joder.stock.strategy.service.process.BackTestProcess;
import com.joder.stock.util.JsonUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * @author Joder 2020/8/16 9:28
 */
@Service
public class StockBackTestService {

    private BackTestProcess backTestProcess;
    private StockHistoryRepository stockHistoryRepository;

    public StockBackTestService(BackTestProcess backTestProcess, StockHistoryRepository stockHistoryRepository) {
        this.backTestProcess = backTestProcess;
        this.stockHistoryRepository = stockHistoryRepository;
    }

    public Mono<BackTestResultVO> backTest(BackTestQuery query) {
        return stockHistoryRepository.getHistory(query.getStockCode(), query.getStartDate(), query.getEndDate())
                .collectList()
                .map(e -> {
                    ProcessQuery processQuery = new ProcessQuery(query.getInitMoney(), query.getStrategyCode(), e, query.getHyperParams());
                    return Map.of("result", backTestProcess.doProcess(processQuery), "stockList", e);
                })
                .map(this::parseResult);
    }

    @SuppressWarnings("unchecked")
    private BackTestResultVO parseResult(Map<String, Object> map) {
        ProcessResult processResult = (ProcessResult) map.get("result");
        List<StockHistory> stockList = (List<StockHistory>) map.get("stockList");
        BackTestResultVO result = new BackTestResultVO(processResult);
        result.setStockList(stockList);
        return result;
    }

    public Flux<Map<String, String>> getStrategyList() {
        return Flux.fromArray(StockStrategy.values())
                .map(e -> Map.of("name", e.getName(), "code", e.getCode()));
    }

    public Flux<Map<String, Object>> getStrategyParams() {
        return Mono.<String>create(sink -> {
            String content = ResourceUtil.readStr("./strategyParams.json", Charset.defaultCharset());
            sink.success(content);
        })
                .subscribeOn(Schedulers.elastic())
                .map(e -> JsonUtil.parse(e, new TypeReference<List<Map<String, Object>>>() {
                }))
                .flatMapMany(Flux::fromIterable);
    }
}
