package org.joder.stock.request.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author Joder 2020/8/19 20:36
 */
@Data
public class StockQuery {

    @JsonProperty("api_name")
    private String apiName;
    private String token;
    private Map<String, Object> params;
    private String fields;
}
