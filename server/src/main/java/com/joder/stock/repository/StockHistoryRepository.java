package com.joder.stock.repository;

import com.joder.stock.model.entity.StockHistory;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Joder 2020/8/8 21:20
 */
@Repository
public interface StockHistoryRepository extends ReactiveMongoRepository<StockHistory, ObjectId> {

    @Query("{'code': '?0', 'day': {$gte: '?1', $lte: '?2'} }")
    Flux<StockHistory> getHistory(String code, String startDate, String endDate);

    Mono<Void> deleteStockHistoryByDay(String day);
}
