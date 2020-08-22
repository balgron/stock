package com.joder.stock.strategy.service.process;

import com.joder.stock.strategy.domain.ProcessQuery;
import com.joder.stock.strategy.domain.ProcessResult;
import com.joder.stock.strategy.domain.TradeInfo;

/**
 * @author Joder 2020/8/14 21:21
 */
public interface BackTestProcess {

    /**
     * 根据策略回测交易数据
     *
     * @param query 查询条件
     * @return 汇总结果
     */
    ProcessResult doProcess(ProcessQuery query);

    /**
     * 预测最后一天的交易情况
     *
     * @param query 查询条件
     * @return 交易信息
     */
    TradeInfo predictLast(ProcessQuery query);
}
