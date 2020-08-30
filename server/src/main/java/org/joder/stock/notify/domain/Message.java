package org.joder.stock.notify.domain;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Joder 2020/8/26 23:26
 */
@Data
public class Message<T> {

    private String date;
    private String type;
    private LinkedHashMap<String, String> titles;
    private List<T> data;
    private String extend;
}
