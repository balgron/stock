package org.joder.stock.model.query;

import lombok.Data;

@Data
public class IncomeQuery {

    private String startDate;
    private String endDate;
    private Byte type;
    private int[] tags;

}
