package ru.sevavto.stock.crm.model.dto.analytic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class OrdersStatisticForPeriodResponse {

    private int countOfOrders;
    private BigDecimal sumOrderPrice;
    private BigDecimal avgOrderPrice;
}
