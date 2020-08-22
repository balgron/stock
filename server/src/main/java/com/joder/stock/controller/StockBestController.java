package com.joder.stock.controller;

import com.joder.stock.model.entity.StockRunResult;
import com.joder.stock.model.entity.StockStrategy;
import com.joder.stock.model.query.BackTestQuery;
import com.joder.stock.model.vo.StockStrategyResultVO;
import com.joder.stock.service.StockStrategyService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Joder 2020/8/22 21:45
 */
@RestController
@RequestMapping("/stock_search_best")
public class StockBestController {

    private StockStrategyService stockStrategyService;

    public StockBestController(StockStrategyService stockStrategyService) {
        this.stockStrategyService = stockStrategyService;
    }

    @GetMapping("/history")
    public Flux<StockStrategy> stockStrategyHistory() {
        return stockStrategyService.getRunHistory();
    }

    @GetMapping("/history/{id}")
    public Mono<StockStrategy> stockStrategyHistoryById(@PathVariable String id) {
        return stockStrategyService.getRunHistory(id);
    }


    @PostMapping("/run")
    public Mono<Boolean> runSearchBest(@RequestBody BackTestQuery query) {
        return stockStrategyService.runBackTest(query);
    }

    @PostMapping("/re_run")
    public Mono<Boolean> reRunSearchBest(String id) {
        return stockStrategyService.reRun(id);
    }

    @GetMapping("/result")
    public Flux<StockStrategyResultVO> runResultList(String id) {
        return stockStrategyService.getStockResult(id);
    }

    @GetMapping("/result/detail")
    public Flux<StockRunResult> runResultDetail(String id, String tsCode) {
        return stockStrategyService.getResultDetail(id, tsCode);
    }
}
