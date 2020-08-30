package org.joder.stock.repository;

import org.bson.types.ObjectId;
import org.joder.stock.model.entity.StockReal;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Joder 2020/8/29 18:46
 */
@Repository
public interface StockRealRepository extends ReactiveMongoRepository<StockReal, ObjectId> {
}
