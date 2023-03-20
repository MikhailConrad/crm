package ru.sevavto.stock.crm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private String productCode;
    private String name;
    private String description;
    private String productCategory;
    private double weight;
    private int quantity;
    private BigDecimal price;
}
