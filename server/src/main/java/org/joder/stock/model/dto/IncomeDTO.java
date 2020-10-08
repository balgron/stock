package org.joder.stock.model.dto;

import lombok.Data;
import org.bson.types.ObjectId;
import org.joder.stock.model.constant.ConsumeEnum;
import org.joder.stock.model.entity.Income;

import java.util.List;

@Data
public class IncomeDTO {

    private String id;
    private String date;
    private Long time;
    private Double money;
    private Byte type;
    private List<Integer> tags;
    private String description;

    public IncomeDTO() {
    }

    public IncomeDTO(Income income) {
        id = income.getId().toHexString();
        date = income.getDate();
        time = income.getTime();
        money = income.getMoney();
        type = income.getType();
        tags = ConsumeEnum.to(income.getTags());
        description = income.getDescription();
    }

    public Income toIncome() {
        return Income.builder()
                .id(id != null ? new ObjectId(id) : null)
                .date(date)
                .time(time)
                .money(money)
                .type(type)
                .tags(ConsumeEnum.from(tags))
                .description(description)
                .build();
    }

}
