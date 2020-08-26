package org.joder.stock.process;

import org.joder.stock.model.dto.StockDTO;
import org.joder.stock.model.dto.StockHistoryDTO;
import org.joder.stock.request.domain.StockApi;
import org.joder.stock.request.service.StockRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Joder 2020/8/19 21:35
 */
@SpringBootTest
class TestStockRequest {

    @Autowired
    private StockRequestService stockRequestService;

    @Test
    void testStockBasic() {
        Assert.notEmpty(stockRequestService.request(StockApi.STOCK_BASIC, null, StockDTO.class), "不为空");
    }

    @Test
    void testDaily() {
        Map<String, Object> map = new HashMap<>();
        map.put("trade_date", "20200819");
        List<StockHistoryDTO> request = stockRequestService.request(StockApi.DAILY, map, StockHistoryDTO.class);
        System.out.println(request);
        Assert.notEmpty(request, "不为空");
    }
}
