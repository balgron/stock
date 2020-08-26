package org.joder.stock.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.joder.stock.core.domain.ProcessResult;
import org.joder.stock.core.domain.TradeInfo;
import org.joder.stock.model.entity.StockHistory;
import org.joder.stock.model.query.BackTestQuery;

import java.util.List;

/**
 * 股票回测记录的结果，模拟交易的详细信息
 *
 * @author Joder 2020/8/16 9:29
 */
@Data
public class BackTestResultVO {

    @JsonIgnore
    private BackTestQuery query;

    private List<StockHistory> stockList;
    /**
     * 利润率
     */
    private Double profit;

    private Double normalProfit;
    /**
     * 总交易次数
     */
    private Integer tradeNum;
    /**
     * 平均每笔回报利润率
     */
    private Double meanProfit;
    /**
     * 最大获利
     */
    private Double maxProfit;
    /**
     * 最大亏损
     */
    private Double maxLoss;
    /**
     * 盈亏比
     */
    private Double profitLossRate;
    /**
     * 盈利笔数
     */
    private Integer profitNum;
    /**
     * 亏损笔数
     */
    private Integer lossNum;
    /**
     * 盈利比例
     */
    private Double profitRate;
    /**
     * 最大连续亏损笔数
     */
    private Integer maxLossNum;
    /**
     * 最大连续亏损金额
     */
    private Double maxLossMoney;

    private List<TradeInfo> infos;

    public BackTestResultVO(ProcessResult processResult) {
        this.infos = processResult.getInfos();
        if (processResult.getInfos().isEmpty()) {
            return;
        }
        this.profit = (processResult.getMoney() - processResult.getInitMoney()) / processResult.getInitMoney();
        this.tradeNum = processResult.getInfos().size();
        this.meanProfit = this.profit / this.tradeNum;
        this.maxProfit = this.findMaxProfit(processResult.getInitMoney());
        this.maxLoss = this.findMaxLoss(processResult.getInitMoney());
        this.profitLossRate = this.findProfitLossRate();
        this.profitNum = this.findProfitNum();
        this.lossNum = this.findLossNum();
        this.profitRate = (this.profitNum * 1.0) / (this.profitNum + this.lossNum);
        this.maxLossNum = this.findMaxLossNum();
        this.maxLossMoney = this.findMaxLossMoney();
    }

    private double findMaxProfit(double initMoney) {
        double max = Double.MIN_VALUE;
        for (TradeInfo info : infos) {
            if (info.isSale()) {
                max = Math.max(max, (info.getMoney() - info.getOriginMoney()) * info.getVolume() - info.getTax());
            }
        }
        return max / initMoney;
    }

    private double findMaxLoss(double initMoney) {
        double min = Double.MAX_VALUE;
        for (TradeInfo info : infos) {
            if (info.isSale()) {
                min = Math.min(min, (info.getMoney() - info.getOriginMoney()) * info.getVolume() - info.getTax());
            }
        }
        return min / initMoney;
    }

    private double findProfitLossRate() {
        double profit = 0;
        double loss = 0;
        for (TradeInfo info : infos) {
            if (!info.isSale()) {
                continue;
            }
            double money = (info.getMoney() - info.getOriginMoney()) * info.getVolume() - info.getTax();
            if (money < 0) {
                loss -= money;
            } else {
                profit += money;
            }
        }
        return loss > 0 ? profit / loss : 0;
    }

    private int findProfitNum() {
        int num = 0;
        for (TradeInfo info : infos) {
            if (info.isSale() && ((info.getMoney() - info.getOriginMoney()) * info.getVolume() - info.getTax()) > 0) {
                num++;
            }
        }
        return num;
    }

    private int findLossNum() {
        int num = 0;
        for (TradeInfo info : infos) {
            if (info.isSale() && ((info.getMoney() - info.getOriginMoney()) * info.getVolume() - info.getTax()) < 0) {
                num++;
            }
        }
        return num;
    }

    private int findMaxLossNum() {
        int max = 0;
        int num = 0;
        for (TradeInfo info : infos) {
            if (!info.isSale()) {
                continue;
            }
            double money = (info.getMoney() - info.getOriginMoney()) * info.getVolume() - info.getTax();
            if (money < 0) {
                num++;
            } else {
                max = Math.max(max, num);
                num = 0;
            }
        }
        max = Math.max(max, num);
        return max;
    }

    private double findMaxLossMoney() {
        double max = 0.;
        double num = 0.;
        for (TradeInfo info : infos) {
            if (!info.isSale()) {
                continue;
            }
            double money = (info.getMoney() - info.getOriginMoney()) * info.getVolume() - info.getTax();
            if (money < 0) {
                num -= money;
            } else {
                max = Math.max(max, num);
                num = 0.;
            }
        }
        max = Math.max(max, num);
        return max;
    }

}
