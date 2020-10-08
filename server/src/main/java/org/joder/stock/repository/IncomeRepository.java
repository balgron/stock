package org.joder.stock.repository;

import org.bson.types.ObjectId;
import org.joder.stock.model.entity.Income;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface IncomeRepository extends ReactiveMongoRepository<Income, ObjectId> {

    @Query(value = "{'date': {$gte: '?0', $lte: '?1'} }", sort = "{'time': -1}")
    Flux<Income> getList(String startDate, String endDate);
}
