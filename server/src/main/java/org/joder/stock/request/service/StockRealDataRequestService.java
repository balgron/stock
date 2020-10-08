package org.joder.stock.request.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.joder.stock.model.config.AppConfig;
import org.joder.stock.request.domain.StockRealData;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Joder 2020/8/29 16:35
 */
@Slf4j
@Service
public class StockRealDataRequestService {

    private WebClient webClient;
    private AppConfig appConfig;
    private Pattern pattern = Pattern.compile("var .*_.*_(.*)=\"(.*)\";");

    public StockRealDataRequestService(WebClient webClient, AppConfig appConfig) {
        this.webClient = webClient;
        this.appConfig = appConfig;
    }

    public List<StockRealData> request(List<String> stockCodes) {
        if (stockCodes.isEmpty()) {
            return new ArrayList<>();
        }
        return Flux.fromIterable(stockCodes)
                .buffer(500)
                .flatMap(e -> {
                    String str = CollectionUtil.join(e, ",");
                    return webClient.get()
                            .uri(appConfig.getSinaApi() + "list=" + str)
                            .retrieve().bodyToMono(String.class);
                }).map(this::parse)
                .reduce((x1, x2) -> {
                    List<StockRealData> list = new ArrayList<>(x1);
                    list.addAll(x2);
                    return list;
                }).block();
    }


    private List<StockRealData> parse(String str) {
        log.info("data: {}", str);
        Matcher matcher = pattern.matcher(str);
        List<StockRealData> ret = new ArrayList<>();
        while (matcher.find()) {
            if (matcher.groupCount() != 2) {
                continue;
            }
            String tsCode = matcher.group(1).trim();
            String[] splitStr = matcher.group(2).trim().split(",");
            ret.add(
                    StockRealData.builder()
                            .tsCode(tsCode)
                            .currentPrice(Double.parseDouble(splitStr[3].trim()))
                            .open(Double.parseDouble(splitStr[1].trim()))
                            .high(Double.parseDouble(splitStr[4].trim()))
                            .low(Double.parseDouble(splitStr[5].trim()))
                            .volume(Double.parseDouble(splitStr[8].trim()))
                            .amount(Double.parseDouble(splitStr[9].trim()))
                            .buyNum1(Double.parseDouble(splitStr[10].trim()))
                            .buyPrice1(Double.parseDouble(splitStr[11].trim()))
                            .buyNum2(Double.parseDouble(splitStr[12].trim()))
                            .buyPrice2(Double.parseDouble(splitStr[13].trim()))
                            .buyNum3(Double.parseDouble(splitStr[14].trim()))
                            .buyPrice3(Double.parseDouble(splitStr[15].trim()))
                            .buyNum4(Double.parseDouble(splitStr[16].trim()))
                            .buyPrice4(Double.parseDouble(splitStr[17].trim()))
                            .buyNum5(Double.parseDouble(splitStr[18].trim()))
                            .buyPrice5(Double.parseDouble(splitStr[19].trim()))
                            .saleNum1(Double.parseDouble(splitStr[20].trim()))
                            .salePrice1(Double.parseDouble(splitStr[21].trim()))
                            .saleNum2(Double.parseDouble(splitStr[22].trim()))
                            .salePrice2(Double.parseDouble(splitStr[23].trim()))
                            .saleNum3(Double.parseDouble(splitStr[24].trim()))
                            .salePrice3(Double.parseDouble(splitStr[25].trim()))
                            .saleNum4(Double.parseDouble(splitStr[26].trim()))
                            .salePrice4(Double.parseDouble(splitStr[27].trim()))
                            .saleNum5(Double.parseDouble(splitStr[28].trim()))
                            .salePrice5(Double.parseDouble(splitStr[29].trim()))
                            .time(DateUtil.parseDateTime(splitStr[30].trim() + " " + splitStr[31].trim()).getTime())
                            .build()
            );
        }
        return ret;
    }
}
