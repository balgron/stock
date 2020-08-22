package com.joder.stock.strategy.domain;

import com.joder.stock.model.entity.StockHistory;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @author Joder 2020/8/14 20:45
 */
public class Process {

    /**
     * 股票列表
     */
    private List<StockHistory> stockList;
    /**
     * 当前位置
     */
    private int index;
    /**
     * 其它参数
     */
    private Map<String, Object> hyperParams;
    /**
     * 上下文
     */
    private Map<String, Object> context;

    private ProcessResult processResult;

    public Process(List<StockHistory> stockList) {
        Assert.notEmpty(stockList, "股票历史价格信息不能为空");
        stockList.sort(Comparator.comparing(StockHistory::getDay));
        this.stockList = stockList;
        index = -1;
        hyperParams = new HashMap<>();
        context = new LinkedHashMap<>();
    }

    public Process(List<StockHistory> stockList, Map<String, Object> hyperParams) {
        Assert.notEmpty(stockList, "股票历史价格信息不能为空");
        stockList.sort(Comparator.comparing(StockHistory::getDay));
        this.stockList = stockList;
        index = -1;
        if (hyperParams != null) {
            this.hyperParams = hyperParams;
        } else {
            this.hyperParams = new HashMap<>();
        }
        context = new LinkedHashMap<>();
    }

    @SuppressWarnings("all")
    public <T> T getContext(String key, Class<T> clazz) {
        Object obj = context.get(key);
        return obj == null ? null : (T) obj;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public boolean containContext(String key) {
        return context.containsKey(key);
    }

    public boolean removeContext(String key) {
        return context.remove(key) != null;
    }

    public void setContext(String key, Object value) {
        context.put(key, value);
    }

    public Map<String, Object> getHyperParams() {
        return hyperParams;
    }

    public void next() {
        index++;
    }

    public boolean hasNext() {
        return index + 1 < stockList.size();
    }

    public int getIndex() {
        return index;
    }

    public StockHistory getCurrentStock() {
        return getStock(index);
    }

    public StockHistory getStock(int index) {
        if (index < 0 || index > this.index || index >= stockList.size()) {
            return null;
        }
        return stockList.get(index);
    }

    public StockHistory getLast() {
        return getStock(stockList.size() - 1);
    }

    public ProcessResult getProcessResult() {
        return processResult;
    }

    public void setProcessResult(ProcessResult processResult) {
        this.processResult = processResult;
    }
}
