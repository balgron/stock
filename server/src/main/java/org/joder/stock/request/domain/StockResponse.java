package org.joder.stock.request.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Joder 2020/8/19 20:37
 */
@Data
public class StockResponse {
    @JsonProperty("request_id")
    private String requestId;
    private Integer code;
    private String msg;
    private StockResponseData data;
}
