package ru.sevavto.stock.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sevavto.stock.crm.model.dto.analytic.OrdersStatisticByMonthResponse;
import ru.sevavto.stock.crm.model.dto.analytic.OrdersStatisticForPeriodResponse;
import ru.sevavto.stock.crm.service.OrderAnalyticService;

import java.util.List;

@RestController
@RequestMapping("/analytic/order")
public class OrderAnalyticController {
    private final OrderAnalyticService orderAnalyticService;

    @Autowired
    public OrderAnalyticController(OrderAnalyticService orderAnalyticService) {
        this.orderAnalyticService = orderAnalyticService;
    }

    @GetMapping(value = "/statistic_by_period", params = {"startDate", "endDate"})
    public OrdersStatisticForPeriodResponse getOrdersStatisticForPeriod(@RequestParam String startDate,
                                                                        @RequestParam String endDate) {
        return orderAnalyticService.getOrdersStatisticForPeriod(startDate, endDate);
    }

    @GetMapping(value = "/statistic_by_month", params = {"startDate", "endDate"})
    public List<OrdersStatisticByMonthResponse> getOrdersStatisticByMonth(@RequestParam String startDate,
                                                                          @RequestParam String endDate) {
        return orderAnalyticService.getOrdersStatisticByMonth(startDate, endDate);
    }
}
