package org.joder.stock.controller;

import org.joder.stock.model.dto.IncomeDTO;
import org.joder.stock.model.query.IncomeQuery;
import org.joder.stock.service.IncomeService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/income")
public class IncomeController {

    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @GetMapping
    public Flux<IncomeDTO> getList(IncomeQuery query) {
        return incomeService.getList(query);
    }

    @PostMapping
    public Mono<Boolean> save(@RequestBody IncomeDTO incomeDTO) {
        return incomeService.save(incomeDTO);
    }

    @PutMapping
    public Mono<Boolean> edit(@RequestBody IncomeDTO incomeDTO) {
        return incomeService.edit(incomeDTO);
    }

    @DeleteMapping("/{id}")
    public Mono<Boolean> delete(@PathVariable String id) {
        return incomeService.delete(id);
    }

    @GetMapping("/income_type")
    public Flux<Map<String, Object>> getIncomeType() {
        return incomeService.getIncomeType();
    }

    @GetMapping("/consume_type")
    public Flux<Map<String, Object>> getConsumeType() {
        return incomeService.getConsumeType();
    }

}
