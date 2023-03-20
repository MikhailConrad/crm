package ru.sevavto.stock.crm.model.dto.analytic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Month;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class OrdersStatisticByMonthResponse extends OrdersStatisticForPeriodResponse {

    private int year;
    private Month month;
}
