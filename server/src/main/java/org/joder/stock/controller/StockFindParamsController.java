package org.joder.stock.controller;

import org.joder.stock.model.entity.BestParam;
import org.joder.stock.model.query.BestParamQuery;
import org.joder.stock.service.StockFindParamsService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Joder 2020/8/25 19:40
 */
@RestController
@RequestMapping("/stock_search_best/param")
public class StockFindParamsController {

    private StockFindParamsService stockFindParamsService;

    public StockFindParamsController(StockFindParamsService stockFindParamsService) {
        this.stockFindParamsService = stockFindParamsService;
    }

    @GetMapping("")
    public Flux<BestParam> bestParamFlux() {
        return stockFindParamsService.getBestParams();
    }

    @PostMapping("/run")
    public Mono<Boolean> bestParamFlux(@RequestBody BestParamQuery query) {
        return stockFindParamsService.runFindParam(query);
    }
}
