package org.joder.stock.controller;

import org.joder.stock.model.entity.Stock;
import org.joder.stock.service.StockService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 股票基础信息
 *
 * @author Joder 2020/8/8 21:30
 */
@RestController
@RequestMapping("/stock")
public class StockController {

    private StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("")
    public Flux<Stock> getStockList(String code) {
        return stockService.getStockList(code);
    }

    @GetMapping("/{code}")
    private Mono<Stock> getStock(@PathVariable String code) {
        return stockService.getStock(code);
    }
}
