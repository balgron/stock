package com.joder.stock.request.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Joder 2020/8/19 20:48
 */
@Data
public class StockResponseData {
    private List<String> fields;
    private List<List<Object>> items;
    @JsonProperty("has_more")
    private Boolean hasMore;
}
