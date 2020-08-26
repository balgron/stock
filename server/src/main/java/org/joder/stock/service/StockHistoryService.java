package org.joder.stock.service;

import org.joder.stock.model.entity.StockHistory;
import org.joder.stock.repository.StockHistoryRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * @author Joder 2020/8/8 21:28
 */
@Service
public class StockHistoryService {

    private StockHistoryRepository stockHistoryRepository;

    public StockHistoryService(StockHistoryRepository stockHistoryRepository) {
        this.stockHistoryRepository = stockHistoryRepository;
    }

    public Flux<StockHistory> getStockHistory(String code, String startDate, String endDate) {
        return stockHistoryRepository.getHistory(code, startDate, endDate);
    }
}
