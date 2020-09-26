package org.joder.stock.model.vo;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;
import org.joder.stock.core.domain.ManualReturn;
import org.joder.stock.core.domain.StockSuggest;
import org.joder.stock.core.domain.TradeReturn;

/**
 * @author Joder 2020/9/13 18:03
 */
@Data
public class StockSimulationVO {

    private TradeReturn machine;
    /**
     * 初始化金额
     */
    private double initMoney;
    /**
     * 最大购买金额
     */
    private double holdMoney;
    private int holdVolume;
    /**
     * 交易日期
     */
    private String date;
    /**
     * 交易量
     */
    private int volume;
    /**
     * 每股价格
     */
    private double money;
    /**
     * 每手持有价格
     */
    private double originMoney;

    private String operate;

    private double tax;

    public StockSimulationVO(ManualReturn ret) {
        BeanUtil.copyProperties(ret, this);
        operate = ret.getStockSuggest() == StockSuggest.SALE ? "sale" : (ret.getStockSuggest() == StockSuggest.NOTHING ? "nothing" : "buy");
    }
}
