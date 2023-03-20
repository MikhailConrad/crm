package ru.sevavto.stock.crm.model.dto.analytic.details;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRatingDetails {

    private int countOfSold;
    private BigDecimal salesProfit;
}
