package org.joder.stock.service;

import cn.hutool.core.io.resource.ResourceUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.joder.stock.core.StockStrategy;
import org.joder.stock.core.domain.ProcessQuery;
import org.joder.stock.core.domain.ProcessResult;
import org.joder.stock.core.domain.StockData;
import org.joder.stock.core.service.process.BackTestProcess;
import org.joder.stock.core.util.JsonUtil;
import org.joder.stock.model.entity.StockHistory;
import org.joder.stock.model.query.BackTestQuery;
import org.joder.stock.model.vo.BackTestResultVO;
import org.joder.stock.repository.StockHistoryRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                    ProcessQuery processQuery = new ProcessQuery(
                            query.getInitMoney(),
                            query.getStrategyCode(),
                            e.stream().map(item -> new StockData(item.getDay(), item.getOpen(), item.getClose(), item.getHigh(), item.getLow(), item.getVolume()))
                                    .collect(Collectors.toList()),
                            query.getHyperParams()
                    );
                    double normalProfit = 0.0;
                    if (!e.isEmpty()) {
                        StockHistory stockHistory1 = e.get(0);
                        StockHistory stockHistory2 = e.get(e.size() - 1);
                        normalProfit = (stockHistory2.getClose() - stockHistory1.getClose()) / stockHistory1.getClose();
                    }
                    return Map.of("result", backTestProcess.doProcess(processQuery), "stockList", e, "normalProfit", normalProfit);
                })
                .map(e -> parseResult(e, query));
    }

    @SuppressWarnings("unchecked")
    private BackTestResultVO parseResult(Map<String, Object> map, BackTestQuery query) {
        ProcessResult processResult = (ProcessResult) map.get("result");
        List<StockHistory> stockList = (List<StockHistory>) map.get("stockList");
        BackTestResultVO result = new BackTestResultVO(processResult);
        result.setStockList(stockList);
        result.setQuery(query);
        result.setNormalProfit((Double) map.get("normalProfit"));
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
