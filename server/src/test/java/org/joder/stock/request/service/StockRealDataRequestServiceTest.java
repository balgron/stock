package org.joder.stock.request.service;

import org.joder.stock.model.entity.Stock;
import org.joder.stock.repository.StockRepository;
import org.joder.stock.request.domain.StockRealData;
import org.joder.stock.service.StockNotifyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Joder 2020/8/29 17:34
 */
@SpringBootTest
@ActiveProfiles("dev")
class StockRealDataRequestServiceTest {

    @Autowired
    private StockRealDataRequestService service;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private StockNotifyService stockNotifyService;

    @Test
    void request() {
        List<Stock> block = stockRepository.findAll().collectList().block();
        List<StockRealData> data = service.request(block.stream().map(Stock::getTsCode).collect(Collectors.toList()));
        System.out.println(data.size());
        Assert.notEmpty(data, "数据为空");
    }

    @Test
    void testNotify() {
        stockNotifyService.notifyStock().block();
    }
}