package org.joder.stock.model.constant;

import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

public enum ConsumeEnum {
    FINANCIAL("#ff7875", "理财", 1),
    MOBILE("#ff9c6e", "移动支出", 1 << 1),
    FOOD("#ffc069", "餐饮支出", 1 << 2),
    OTHER("#d9d9d9", "其它", 1 << 3),
    SALARY("#ffc53d", "工资", 1 << 4),
    ENTERTAINMENT("#ffec3d", "娱乐支出", 1 << 5),
    DAILY("#bae637", "日常支出", 1 << 6),
    RENT("#36cfc9", "房租", 1 << 7);

    private String color;
    private String text;
    private int value;


    ConsumeEnum(String color, String text, int value) {
        this.color = color;
        this.text = text;
        this.value = value;
    }

    public static int from(@NonNull List<Integer> value) {
        int num = 0;
        for (int v : value) {
            num |= v;
        }
        return num;
    }

    public static boolean has(int tags, @NonNull int[] value) {
        for (int v : value) {
            if ((tags & v) == 0) {
                return false;
            }
        }
        return true;
    }

    public static List<Integer> to(int num) {
        List<Integer> list = new ArrayList<>();
        for (ConsumeEnum consumeEnum : values()) {
            if ((num & consumeEnum.value) != 0) {
                list.add(consumeEnum.value);
            }
        }
        return list;
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    public String getColor() {
        return color;
    }
}
