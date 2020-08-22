package com.joder.stock.controller;

import com.joder.stock.model.entity.StockHistory;
import com.joder.stock.service.StockHistoryService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * @author Joder 2020/8/8 21:30
 */
@RestController
@RequestMapping("/stock")
public class StockHistoryController {

    private StockHistoryService stockHistoryService;

    public StockHistoryController(StockHistoryService stockHistoryService) {
        this.stockHistoryService = stockHistoryService;
    }

    @GetMapping("/history/{code}")
    public Flux<StockHistory> getHistory(@PathVariable String code,
                                         @RequestParam String startDate,
                                         @RequestParam String endDate) {
        return stockHistoryService.getStockHistory(code, startDate, endDate);
    }
}
