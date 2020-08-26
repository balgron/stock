package org.joder.stock.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Joder 2020/8/16 9:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BackTestQuery {

    /**
     * 初始金额
     */
    private double initMoney;
    /**
     * 策略
     */
    private String strategyCode;
    /**
     * 附加参数
     */
    private Map<String, Object> hyperParams;
    /**
     * 股票代号
     */
    private String stockCode;
    /**
     * 回测开始时间
     */
    private String startDate;
    /**
     * 回测结束时间
     */
    private String endDate;

}
