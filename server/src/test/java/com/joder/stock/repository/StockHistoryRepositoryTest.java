package com.joder.stock.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Joder 2020/8/19 23:46
 */
@SpringBootTest
class StockHistoryRepositoryTest {

    @Autowired
    private StockHistoryRepository stockHistoryRepository;

    @Test
    void deleteStockHistoryByDay() {

        stockHistoryRepository.deleteStockHistoryByDay("2020-08-19").subscribe();
    }
}