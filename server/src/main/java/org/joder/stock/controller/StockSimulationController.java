package org.joder.stock.controller;

import org.joder.stock.core.domain.TradeReturn;
import org.joder.stock.model.query.BackTestQuery;
import org.joder.stock.model.query.StockSimulationQuery;
import org.joder.stock.model.vo.StockSimulationVO;
import org.joder.stock.service.StockBackTestService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author Joder 2020/9/13 19:45
 */
@RestController
@RequestMapping("/stock_simulation")
public class StockSimulationController {

    private final StockBackTestService stockBackTestService;

    public StockSimulationController(StockBackTestService stockBackTestService) {
        this.stockBackTestService = stockBackTestService;
    }

    @PostMapping("/suggest")
    public Mono<TradeReturn> suggest(@RequestBody BackTestQuery query) {
        return stockBackTestService.suggestLast(query);
    }

    @PostMapping("/simulation")
    public Mono<StockSimulationVO> simulation(@RequestBody StockSimulationQuery query) {
        return stockBackTestService.simulation(query);
    }
}
