package com.joder.stock.repository;

import com.joder.stock.model.entity.StockStrategy;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * @author Joder 2020/8/22 17:37
 */
public interface StockStrategyRepository extends ReactiveMongoRepository<StockStrategy, ObjectId> {
}
