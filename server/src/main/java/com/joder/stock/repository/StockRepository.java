package com.joder.stock.repository;

import com.joder.stock.model.entity.Stock;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Joder 2020/8/8 21:26
 */
@Repository
public interface StockRepository extends ReactiveMongoRepository<Stock, ObjectId> {

    @Query(value = "{'ts_code' : '?0'}")
    Mono<Stock> findByTsCode(String code);

    @Query(value = "{'ts_code': {'$regex': /?0/i}}", sort = "{symbol : 1}")
    Flux<Stock> all(String code);

}
