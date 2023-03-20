package ru.sevavto.stock.crm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPositionResponse {

    private long id;
    private ProductResponse product;
    private int amount;
}
