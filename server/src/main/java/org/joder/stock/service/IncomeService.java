package org.joder.stock.service;

import cn.hutool.core.map.MapUtil;
import org.bson.types.ObjectId;
import org.joder.stock.model.constant.ConsumeEnum;
import org.joder.stock.model.constant.IncomeTypeEnum;
import org.joder.stock.model.dto.IncomeDTO;
import org.joder.stock.model.entity.Income;
import org.joder.stock.model.query.IncomeQuery;
import org.joder.stock.repository.IncomeRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;

    public IncomeService(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }

    public Mono<Boolean> save(IncomeDTO income) {
        return incomeRepository.insert(income.toIncome())
                .map(e -> true)
                .onErrorReturn(false);
    }

    public Mono<Boolean> edit(IncomeDTO income) {
        return incomeRepository.save(income.toIncome())
                .map(e -> true)
                .onErrorReturn(false);
    }

    public Mono<Boolean> delete(String id) {
        return incomeRepository.delete(Income.builder().id(new ObjectId(id)).build())
                .map(e -> true)
                .onErrorReturn(false);
    }

    public Flux<IncomeDTO> getList(IncomeQuery query) {
        return incomeRepository.getList(query.getStartDate(), query.getEndDate())
                .filter(e -> query.getTags() == null || ConsumeEnum.has(e.getTags(), query.getTags()))
                .filter(e -> query.getType() == null || query.getType().equals(e.getType()))
                .map(IncomeDTO::new);
    }

    public Flux<Map<String, Object>> getIncomeType() {
        return Flux.fromArray(IncomeTypeEnum.values())
                .map(e -> MapUtil.<String, Object>builder("text", e.getText()).put("value", e.getValue()).build());
    }

    public Flux<Map<String, Object>> getConsumeType() {
        return Flux.fromArray(ConsumeEnum.values())
                .map(e ->
                        MapUtil.<String, Object>builder("text", e.getText())
                                .put("value", e.getValue())
                                .put("color", e.getColor())
                                .build()
                );
    }

}
