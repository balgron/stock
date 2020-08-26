package org.joder.stock.core.service.strategy.macd;

import org.joder.stock.core.domain.StockProcess;
import org.joder.stock.core.domain.StockSuggest;
import org.joder.stock.core.service.strategy.AllInStockStrategyProcess;

import java.util.Map;

/**
 * @author Joder 2020/8/17 23:58
 */
public class MacdStockStrategyProcessImpl implements AllInStockStrategyProcess {


    @Override
    public StockSuggest judgeSale(StockProcess process) {
        int index = process.getIndex();
        if (index == 0) {
            return StockSuggest.NOTHING;
        }
        double macd = macd(process);
        Double context1 = process.getContext("MACD_" + (index - 1), Double.TYPE);
        Double diff1 = process.getContext("DIFF_" + index, Double.TYPE);
        Double diff2 = process.getContext("DIFF_" + (index - 1), Double.TYPE);
        if (macd == 0 || context1 == null) {
            return StockSuggest.NOTHING;
        } else if ((diff1 < 0 || diff2 < 0) && macd * context1 <= 0) {
            return StockSuggest.BUY;
        } else if ((diff1 >= 0 || diff2 >= 0) && macd * context1 <= 0) {
            return StockSuggest.SALE;
        }
        return StockSuggest.NOTHING;
    }

    private double macd(StockProcess process) {
        Integer dayLineNum = getValue(process.getHyperParams(), "dayLineNum", 5);
        Integer minDayNum = getValue(process.getHyperParams(), "minDayNum", 12);
        Integer maxDayNum = getValue(process.getHyperParams(), "maxDayNum", 26);
        Integer deaDayNum = getValue(process.getHyperParams(), "deaDayNum", 9);
        int index = process.getIndex();
        mean(process, dayLineNum);
        double meanMin = mean(process.getContext(), process.getIndex(), "MEAN", "MEAN" + minDayNum, minDayNum);
        double meanMax = mean(process.getContext(), process.getIndex(), "MEAN", "MEAN" + maxDayNum, maxDayNum);
        double diff = 0;
        if (meanMax > 0 && meanMin > 0) {
            diff = meanMin - meanMax;
            process.setContext("DIFF_" + index, diff);
        }
        double dea = mean(process.getContext(), index, "DIFF", "DEA", deaDayNum);
        if (dea != 0) {
            process.setContext("MACD_" + index, 2 * (diff - dea));
        }
        return 2 * (diff - dea);
    }

    private double mean(StockProcess process, int dayNum) {
        int index = process.getIndex();
        double ret = 0;
        if (index < dayNum - 1) {
            ret = 0;
        } else {
            for (int i = 0; i < dayNum; i++) {
                ret += process.getStock(index - i).getClose();
            }
            ret /= dayNum;
        }
        process.setContext("MEAN_" + index, ret);
        return ret;
    }

    private double mean(Map<String, Object> map, int index, String findKey, String key, int dayNum) {
        double ret = 0;
        if (index < dayNum - 1) {
            ret = 0;
        } else {
            for (int i = 0; i < dayNum; i++) {
                Double value = getValue(map, findKey + "_" + (index - i), null);
                if (value == null) {
                    ret = 0;
                    break;
                }
                ret += value;
            }
            ret /= dayNum;
        }
        map.put(key + "_" + index, ret);
        return ret;
    }
}
