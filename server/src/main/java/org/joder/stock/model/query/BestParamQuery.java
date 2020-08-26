package org.joder.stock.model.query;

import lombok.Data;

import java.util.Map;

/**
 * @author Joder 2020/8/25 19:26
 */
@Data
public class BestParamQuery {

    private String tsCode;
    private String strategyCode;
    private Integer initMoney;
    private String startDate;
    private String endDate;
    private Map<String, Object[]> hyperParams;
}
