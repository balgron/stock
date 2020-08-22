package com.joder.stock.process;

import com.joder.stock.model.entity.StockHistory;
import com.joder.stock.repository.StockHistoryRepository;
import com.joder.stock.strategy.domain.ProcessQuery;
import com.joder.stock.strategy.domain.ProcessResult;
import com.joder.stock.strategy.service.process.BackTestProcess;
import com.joder.stock.strategy.service.process.impl.DefaultBackTestProcessImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Joder 2020/8/15 15:42
 */
@SpringBootTest
class BackTestTurtleTest {

    @Autowired
    private StockHistoryRepository repository;
    private BackTestProcess backTestProcess = new DefaultBackTestProcessImpl();

    private List<StockHistory> stockList;
    private Map<String, Object> map;

    @BeforeEach
    void before() {
        String code = "sz000006";
        String startDate = "2018-01-01";
        String endDate = "2019-12-31";
        stockList = repository.getHistory(code, startDate, endDate).collectList().block();
        map = new HashMap<>();
        map.put("minDayNum", 10);
        map.put("maxDayNum", 20);
    }

    @Test
    void testTurtleStrategy() {
        ProcessQuery query = new ProcessQuery("turtle", stockList);
        query.setHyperParams(map);
        ProcessResult processResult = backTestProcess.doProcess(query);
        System.out.println(processResult);
    }

    @Test
    void testTurtlePlusStrategy() {
        ProcessQuery query = new ProcessQuery("turtle-plus", stockList);
        query.setHyperParams(map);
        ProcessResult processResult = backTestProcess.doProcess(query);
        System.out.println(processResult);
    }

    @Test
    void testMeanStrategy() {
        ProcessQuery query = new ProcessQuery("mean", stockList);
        query.setHyperParams(map);
        ProcessResult processResult = backTestProcess.doProcess(query);
        System.out.println(processResult);
    }
}
