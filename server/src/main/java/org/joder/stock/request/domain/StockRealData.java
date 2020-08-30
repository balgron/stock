package org.joder.stock.request.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joder.stock.model.entity.StockReal;

/**
 * @author Joder 2020/8/29 16:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockRealData {

    private String tsCode;
    private Long time;
    private Double currentPrice;
    private Double open;
    private Double high;
    private Double low;
    private Double volume;
    private Double amount;

    private Double buyNum1;
    private Double buyPrice1;
    private Double buyNum2;
    private Double buyPrice2;
    private Double buyNum3;
    private Double buyPrice3;
    private Double buyNum4;
    private Double buyPrice4;
    private Double buyNum5;
    private Double buyPrice5;

    private Double saleNum1;
    private Double salePrice1;
    private Double saleNum2;
    private Double salePrice2;
    private Double saleNum3;
    private Double salePrice3;
    private Double saleNum4;
    private Double salePrice4;
    private Double saleNum5;
    private Double salePrice5;

    public StockReal toStockReal() {
        return StockReal.builder()
                .tsCode(tsCode)
                .currentPrice(currentPrice)
                .open(open)
                .high(high)
                .low(low)
                .volume(volume)
                .amount(amount)
                .buyNum1(buyNum1)
                .buyPrice1(buyPrice1)
                .buyNum2(buyNum2)
                .buyPrice2(buyPrice2)
                .buyNum3(buyNum3)
                .buyPrice3(buyPrice3)
                .buyNum4(buyNum4)
                .buyPrice4(buyPrice4)
                .buyNum5(buyNum5)
                .buyPrice5(buyPrice5)
                .saleNum1(saleNum1)
                .salePrice1(salePrice1)
                .saleNum2(saleNum2)
                .salePrice2(salePrice2)
                .saleNum3(saleNum3)
                .salePrice3(salePrice3)
                .saleNum4(saleNum4)
                .salePrice4(salePrice4)
                .saleNum5(saleNum5)
                .salePrice5(salePrice5)
                .time(time)
                .build();
    }
}
