package org.joder.stock.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.joder.stock.core.util.JsonUtil;
import org.joder.stock.model.entity.StockRunResult;
import org.joder.stock.model.entity.StockStrategy;
import org.joder.stock.model.query.BackTestQuery;
import org.joder.stock.model.vo.StockStrategyResultVO;
import org.joder.stock.repository.StockRepository;
import org.joder.stock.repository.StockRunResultRepository;
import org.joder.stock.repository.StockStrategyRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Comparator;

/**
 * @author Joder 2020/8/22 17:36
 */
@Service
@Slf4j
public class StockStrategyService implements InitializingBean {

    /**
     * 用几年作比较
     */
    private static final int NUM = 3;
    private StockStrategyRepository stockStrategyRepository;
    private StockRunResultRepository stockRunResultRepository;
    private StockRepository stockRepository;
    private StockBackTestService stockBackTestService;

    public StockStrategyService(StockStrategyRepository stockStrategyRepository, StockRunResultRepository stockRunResultRepository,
                                StockRepository stockRepository, StockBackTestService stockBackTestService) {
        this.stockStrategyRepository = stockStrategyRepository;
        this.stockRunResultRepository = stockRunResultRepository;
        this.stockRepository = stockRepository;
        this.stockBackTestService = stockBackTestService;
    }

    public Flux<StockStrategy> getRunHistory() {
        return stockStrategyRepository.findAll(
                Sort.sort(StockStrategy.class)
                        .by(StockStrategy::getCreateTime)
                        .descending()
        );
    }

    public Mono<StockStrategy> getRunHistory(String uuid) {
        return stockStrategyRepository.findOne(Example.of(StockStrategy.builder().uuid(uuid).build()));
    }

    public Mono<Boolean> runBackTest(BackTestQuery query) {
        String uuid = UUID.randomUUID().toString();
        StockStrategy stockStrategy = StockStrategy.builder()
                .createTime(System.currentTimeMillis())
                .uuid(uuid)
                .endDate(query.getEndDate())
                .initMoney(query.getInitMoney())
                .strategyCode(query.getStrategyCode())
                .params(JsonUtil.toJson(query.getHyperParams()))
                .state(0)
                .build();
        return stockStrategyRepository.save(stockStrategy)
                .map(e -> runBackground(query, e))
                .map(e -> true)
                .onErrorReturn(false);
    }

    public Mono<Boolean> reRun(String uuid) {
        Mono<StockStrategy> entry = stockStrategyRepository.findOne(Example.of(StockStrategy.builder().uuid(uuid).build()));
        return stockRunResultRepository.delete(StockRunResult.builder().strategyUuid(uuid).build())
                .then(entry)
                .flatMap(e -> {
                    e.setState(0);
                    return stockStrategyRepository.save(e);
                })
                .map(e -> runBackground(parseSave(e), e))
                .map(e -> true)
                .onErrorReturn(false);
    }

    public Flux<StockStrategyResultVO> getStockResult(String uuid) {
        return stockRunResultRepository.getAllByStrategyByUUid(uuid)
                .groupBy(StockRunResult::getTsCode)
                .flatMap(e ->
                        Mono.from(e.buffer(NUM + 1)
                                .map(item -> {
                                    StockStrategyResultVO result = new StockStrategyResultVO();
                                    result.setUuid(uuid);
                                    result.setTsCode(e.key());
                                    item.sort(
                                            Comparator.comparing(StockRunResult::getEndDate)
                                                    .thenComparing(r -> DateUtil.betweenDay(DateUtil.parseDate(r.getStartDate()),
                                                            DateUtil.parseDate(r.getEndDate()), false))
                                    );
                                    result.setOne(item.size() > 0 && item.get(0).getProfit() != null ? item.get(0).getProfit() : 0.0);
                                    result.setTwo(item.size() > 1 && item.get(1).getProfit() != null ? item.get(1).getProfit() : 0.0);
                                    result.setThree(item.size() > 2 && item.get(2).getProfit() != null ? item.get(2).getProfit() : 0.0);
                                    result.setTotal(item.size() > 3 && item.get(3).getProfit() != null ? item.get(3).getProfit() : 0.0);
                                    return result;
                                }))
                );
    }

    public Flux<StockRunResult> getResultDetail(String uuid, String tsCode) {
        return stockRunResultRepository.getAllByStrategy(uuid, tsCode).sort(
                Comparator.comparing(StockRunResult::getEndDate)
                        .thenComparing(r -> DateUtil.betweenDay(DateUtil.parseDate(r.getStartDate()),
                                DateUtil.parseDate(r.getEndDate()), false))
        );
    }

    private BackTestQuery parseSave(StockStrategy stockStrategy) {
        return BackTestQuery.builder()
                .endDate(stockStrategy.getEndDate())
                .strategyCode(stockStrategy.getStrategyCode())
                .hyperParams(JsonUtil.parse(stockStrategy.getParams(), new TypeReference<>() {
                }))
                .initMoney(stockStrategy.getInitMoney())
                .build();
    }

    private Mono<Void> runBackground(BackTestQuery query, StockStrategy stockStrategy) {
        DateTime start = DateUtil.offset(DateUtil.parseDate(query.getEndDate()), DateField.YEAR, -3);
        stockRepository.getAllByBetweenDay(null, DateUtil.formatDate(start))
                .publishOn(Schedulers.elastic())
                .flatMap(e ->
                        Flux.<BackTestQuery>create(fluxSink -> {
                            for (int i = 0; i < NUM; i++) {
                                String startDate = DateUtil.formatDate(DateUtil.offset(start, DateField.YEAR, i));
                                String endDate = DateUtil.formatDate(DateUtil.offset(start, DateField.YEAR, i + 1));
                                fluxSink.next(generateQuery(query, e.getTsCode(), startDate, endDate));
                            }
                            String startDate = DateUtil.formatDate(start);
                            String endDate = query.getEndDate();
                            fluxSink.next(generateQuery(query, e.getTsCode(), startDate, endDate));
                            fluxSink.complete();
                        })
                )
                .flatMap(e -> saveResult(e, stockStrategy.getUuid()))
                .then(Mono.just(stockStrategy))
                .flatMap(e -> {
                    e.setState(1);
                    return stockStrategyRepository.save(e);
                })
                .subscribe();
        return Mono.empty();
    }

    private BackTestQuery generateQuery(BackTestQuery base, String tsCode, String startDate, String endDate) {
        BackTestQuery backTestQuery = BeanUtil.copyProperties(base, BackTestQuery.class);
        backTestQuery.setStartDate(startDate);
        backTestQuery.setEndDate(endDate);
        backTestQuery.setStockCode(tsCode);
        return backTestQuery;
    }

    private Mono<Void> saveResult(BackTestQuery query, String uuid) {
        return stockBackTestService.backTest(query)
                .doOnError(e -> log.info("股票回测失败 {}", query.getStockCode(), e))
                .subscribeOn(Schedulers.elastic())
                .flatMap(e -> {
                    if (CollectionUtil.isEmpty(e.getStockList())) {
                        return Mono.empty();
                    }
                    return stockRunResultRepository.save(
                            StockRunResult.builder()
                                    .strategyUuid(uuid)
                                    .startDate(query.getStartDate())
                                    .endDate(query.getEndDate())
                                    .tsCode(query.getStockCode())
                                    .lossNum(e.getLossNum())
                                    .maxLoss(e.getMaxLoss())
                                    .maxLossMoney(e.getMaxLossMoney())
                                    .maxLossNum(e.getMaxLossNum())
                                    .maxProfit(e.getMaxProfit())
                                    .meanProfit(e.getMeanProfit())
                                    .profit(e.getProfit())
                                    .profitLossRate(e.getProfitLossRate())
                                    .profitNum(e.getProfitNum())
                                    .profitRate(e.getProfitRate())
                                    .tradeNum(e.getTradeNum())
                                    .build()
                    );
                })
                .then();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        stockStrategyRepository.findAll(Example.of(StockStrategy.builder().state(0).build()))
                .flatMap(e -> stockStrategyRepository.deleteById(e.getId()).map(i -> e))
                .flatMap(e -> stockRunResultRepository.deleteAllByStrategyUuid(e.getUuid()).subscribeOn(Schedulers.elastic()))
                .subscribe();
    }
}
