package com.joder.stock.strategy.service.process;

import com.joder.stock.strategy.domain.ProcessQuery;
import com.joder.stock.strategy.domain.ProcessResult;

/**
 * @author Joder 2020/8/14 21:21
 */
public interface BackTestProcess {

    ProcessResult doProcess(ProcessQuery query);
}
