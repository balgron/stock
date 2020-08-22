package com.joder.stock.util;

/**
 * @author Joder 2020/8/15 11:22
 */
public class StockUtil {

    public static final int MIN_BASE = 100;

    private StockUtil() {
    }

    public static int canBuy(double hasMoney, double stockVal, int minBase) {
        return ((int) (hasMoney / (stockVal * minBase))) * minBase;
    }

    public static int canBuy(double hasMoney, double stockVal) {
        return canBuy(hasMoney, stockVal, MIN_BASE);
    }

    public static double saleTax(int volume, double money) {
        double total = volume * money;
        return total * 0.001 + Math.max(5, total * 0.0003) + total * 0.0006;
    }

    public static double buyTax(int volume, double money) {
        double total = volume * money;
        return Math.max(5, total * 0.0003) + total * 0.0006;
    }

    public static String convertCode(String code) {
        int index = code.indexOf(".");
        return code.substring(index + 1).toLowerCase() + code.substring(0, index);
    }

}
