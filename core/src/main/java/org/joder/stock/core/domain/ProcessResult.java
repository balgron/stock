package org.joder.stock.core.domain;

import lombok.Getter;
import lombok.ToString;
import org.joder.stock.core.util.StockUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Joder 2020/8/14 21:22
 */
@Getter
@ToString
public class ProcessResult {
    /**
     * 初始化金额
     */
    private double initMoney;
    /**
     * 剩余金额
     */
    private double money;
    /**
     * 持有股票数量
     */
    private int volume;
    /**
     * 持有股票花费金额
     */
    private double stockValue;

    private StockHoldState stockHoldState;

    private List<TradeInfo> infos;

    public ProcessResult(double initMoney) {
        this.initMoney = initMoney;
        this.money = initMoney;
        this.volume = 0;
        this.stockValue = 0;
        infos = new ArrayList<>();
        stockHoldState = StockHoldState.UN_HOLD;
    }

    public void dealTrade(TradeInfo info) {
        if (info.getVolume() == 0) {
            return;
        }
        int increase = 1;
        if (info.isSale()) {
            increase = -1;
            info.setOriginMoney(stockValue / volume);
            stockValue -= info.getOriginMoney() * info.getVolume();
        }
        double tax;
        if (info.isSale()) {
            tax = StockUtil.saleTax(info.getVolume(), info.getMoney());
            money = money + info.getMoney() * info.getVolume() - tax;
        } else {
            tax = StockUtil.buyTax(info.getVolume(), info.getMoney());
            money = money - info.getMoney() * info.getVolume() - tax;
        }
        info.setTax(tax);
        volume += increase * info.getVolume();
        if (!info.isSale()) {
            stockValue += info.getMoney() * info.getVolume();
            info.setOriginMoney(stockValue / volume);
        }
        infos.add(info);
        stockHoldState = StockHoldState.valueOf(volume);
    }
}
