package org.joder.stock;

import org.joder.stock.model.entity.StockHistory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("股票历史-正常")
    void testStockHistory() {
        webTestClient.get()
                .uri("/stock/history/sz000001?startDate=2020-01-01&endDate=2020-02-01")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StockHistory.class);
    }

    @Test
    @DisplayName("股票历史-错误")
    void testStockHistoryError() {
        webTestClient.get()
                .uri("/stock/history/sz000001?startDate=2020-01-01")
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

}
