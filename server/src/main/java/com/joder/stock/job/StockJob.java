package com.joder.stock.job;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.joder.stock.model.dto.StockDTO;
import com.joder.stock.model.dto.StockHistoryDTO;
import com.joder.stock.model.entity.Stock;
import com.joder.stock.model.entity.StockHistory;
import com.joder.stock.repository.StockHistoryRepository;
import com.joder.stock.repository.StockRepository;
import com.joder.stock.request.domain.StockApi;
import com.joder.stock.request.service.StockRequestService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Joder 2020/8/19 22:51
 */
@Component
public class StockJob {

    private StockRequestService stockRequestService;
    private StockRepository stockRepository;
    private StockHistoryRepository stockHistoryRepository;

    public StockJob(StockRequestService stockRequestService, StockRepository stockRepository, StockHistoryRepository stockHistoryRepository) {
        this.stockRequestService = stockRequestService;
        this.stockRepository = stockRepository;
        this.stockHistoryRepository = stockHistoryRepository;
    }

    @PostConstruct
    public void init() throws IOException {
        List<String> days = days();
        String maxDay = null;
        if (!days.isEmpty()) {
            reloadStockList();
        }
        for (String day : days) {
            String d = reloadHistory(day);
            maxDay = StrUtil.compare(maxDay, d, true) > 0 ? maxDay : d;
        }
        if (maxDay != null) {
            saveDay(maxDay);
        }
    }

    private void reloadStockList() {
        List<Stock> list = stockRequestService.request(StockApi.STOCK_BASIC, null, StockDTO.class)
                .stream()
                .map(StockDTO::toStock)
                .sorted(Comparator.comparing(Stock::getTsCode))
                .collect(Collectors.toList());
        stockRepository.deleteAll()
                .then(stockRepository.saveAll(Flux.fromStream(list.stream())).then())
                .subscribe();
    }

    private String reloadHistory(String day) {
        Map<String, Object> map = new HashMap<>();
        map.put("trade_date", day);
        List<StockHistory> list = stockRequestService.request(StockApi.DAILY, map, StockHistoryDTO.class)
                .stream()
                .map(StockHistoryDTO::toStockHistory)
                .sorted(Comparator.comparing(StockHistory::getCode))
                .collect(Collectors.toList());
        if (!list.isEmpty()) {
            stockHistoryRepository.deleteStockHistoryByDay(day)
                    .then(stockHistoryRepository.saveAll(Flux.fromStream(list.stream())).then())
                    .subscribe();
            return day;
        }
        return null;
    }

    private List<String> days() throws IOException {
        InputStream streamSafe = new FileInputStream("read.day");
        byte[] bytes = streamSafe.readAllBytes();
        Date date;
        if (bytes.length == 0) {
            date = DateUtil.parse("20000101");
        } else {
            date = DateUtil.parse(new String(bytes));
        }
        String now = DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
        int i = 1;
        String start;
        List<String> list = new ArrayList<>();
        while ((start = DateUtil.format(DateUtil.offsetDay(date, i++), DatePattern.PURE_DATE_PATTERN)).compareTo(now) <= 0) {
            int week = DateUtil.dayOfWeek(DateUtil.parse(start));
            if (week > 1 && week < 7) {
                list.add(start);
            }
        }
        return list;
    }

    private void saveDay(String maxDate) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("read.day");
        fileOutputStream.write(maxDate.getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();
    }
}