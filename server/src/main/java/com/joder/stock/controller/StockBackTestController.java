package com.joder.stock.controller;

import com.joder.stock.model.query.BackTestQuery;
import com.joder.stock.model.vo.BackTestResultVO;
import com.joder.stock.service.StockBackTestService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author Joder 2020/8/16 10:35
 */
@RestController
@RequestMapping("/stock_back_test")
public class StockBackTestController {

    private StockBackTestService stockBackTestService;

    public StockBackTestController(StockBackTestService stockBackTestService) {
        this.stockBackTestService = stockBackTestService;
    }

    @PostMapping("")
    public Mono<BackTestResultVO> backTest(@RequestBody BackTestQuery query) {
        return stockBackTestService.backTest(query);
    }

    @GetMapping("/strategy")
    public Flux<Map<String, String>> getStrategy() {
        return stockBackTestService.getStrategyList();
    }

    @GetMapping("/strategy/params")
    public Flux<Map<String, Object>> getStrategyParams() {
        return stockBackTestService.getStrategyParams();
    }
}
