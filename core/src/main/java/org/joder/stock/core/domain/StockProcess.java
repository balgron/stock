package org.joder.stock.core.domain;

import org.springframework.util.Assert;

import java.util.*;

/**
 * @author Joder 2020/8/14 20:45
 */
public class StockProcess {

    /**
     * 股票列表
     */
    private List<StockData> stockList;
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

    public StockProcess(List<StockData> stockList) {
        Assert.notEmpty(stockList, "股票历史价格信息不能为空");
        stockList.sort(Comparator.comparing(StockData::getDay));
        this.stockList = stockList;
        index = -1;
        hyperParams = new HashMap<>();
        context = new LinkedHashMap<>();
    }

    public StockProcess(List<StockData> stockList, Map<String, Object> hyperParams) {
        Assert.notEmpty(stockList, "股票历史价格信息不能为空");
        stockList.sort(Comparator.comparing(StockData::getDay));
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

    public StockData getCurrentStock() {
        return getStock(index);
    }

    public StockData getStock(int index) {
        if (index < 0 || index > this.index || index >= stockList.size()) {
            return null;
        }
        return stockList.get(index);
    }

    public StockData getLast() {
        return getStock(stockList.size() - 1);
    }

    public ProcessResult getProcessResult() {
        return processResult;
    }

    public void setProcessResult(ProcessResult processResult) {
        this.processResult = processResult;
    }
}
