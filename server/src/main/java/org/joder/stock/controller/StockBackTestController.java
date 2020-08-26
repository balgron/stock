package org.joder.stock.controller;

import org.joder.stock.model.query.BackTestQuery;
import org.joder.stock.model.vo.BackTestResultVO;
import org.joder.stock.service.StockBackTestService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 股票回测
 *
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
