package ru.sevavto.stock.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sevavto.stock.crm.model.dto.analytic.OrdersStatisticByMonthResponse;
import ru.sevavto.stock.crm.model.dto.analytic.details.OrdersStatisticDetails;
import ru.sevavto.stock.crm.model.dto.analytic.OrdersStatisticForPeriodResponse;
import ru.sevavto.stock.crm.model.entity.Order;
import ru.sevavto.stock.crm.repository.OrderRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class OrderAnalyticService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderAnalyticService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrdersStatisticForPeriodResponse getOrdersStatisticForPeriod(String startDate, String endDate) {
        List<Order> ordersForPeriod = getOrdersForPeriod(startDate, endDate);
        OrdersStatisticDetails details = new OrdersStatisticDetails(0, BigDecimal.ZERO);
        for(Order order: ordersForPeriod) {
            details.setCountOfOrders(details.getCountOfOrders() + 1);
            details.setSumOrderPrice(details.getSumOrderPrice().add(order.getTotalPrice()));
        }
        BigDecimal avgOrderPrice = details.getSumOrderPrice()
                .divide(BigDecimal.valueOf(details.getCountOfOrders()), 2, RoundingMode.HALF_UP);
        return OrdersStatisticForPeriodResponse.builder()
                .countOfOrders(details.getCountOfOrders())
                .sumOrderPrice(details.getSumOrderPrice())
                .avgOrderPrice(avgOrderPrice)
                .build();
    }

    public List<OrdersStatisticByMonthResponse> getOrdersStatisticByMonth(String startDate, String endDate) {
        List<Order> ordersForPeriod = getOrdersForPeriod(startDate, endDate);
        Map<Integer, Map<Month, OrdersStatisticDetails>> ordersStatisticMap = new HashMap<>();
        for(Order order: ordersForPeriod) {
            int year = order.getDateOfOrder().getYear();
            Month month = order.getDateOfOrder().getMonth();
            if(ordersStatisticMap.containsKey(year)) {
                if(ordersStatisticMap.get(year).containsKey(month)) {
                    OrdersStatisticDetails details = ordersStatisticMap.get(year).get(month);
                    details.setCountOfOrders(details.getCountOfOrders() + 1);
                    details.setSumOrderPrice(details.getSumOrderPrice().add(order.getTotalPrice()));
                } else {
                    ordersStatisticMap.get(year).put(month, OrdersStatisticDetails.builder()
                                    .countOfOrders(1)
                                    .sumOrderPrice(order.getTotalPrice())
                                    .build());
                }
            } else {
                ordersStatisticMap.put(year, new HashMap<>(Map.of(month, OrdersStatisticDetails.builder()
                                .countOfOrders(1)
                                .sumOrderPrice(order.getTotalPrice())
                                .build())));
            }
        }
        List<OrdersStatisticByMonthResponse> ordersStatisticList = new LinkedList<>();
        ordersStatisticMap.forEach(
                (year, monthMap) -> monthMap.forEach(
                        (month, details) -> ordersStatisticList.add(mapToOrderStatisticByMonth(year, month, details))
                )
        );
        //todo: сортировать по годам, затем по месяцам
        ordersStatisticList.sort(Comparator
                .comparing(OrdersStatisticByMonthResponse::getYear)
                .thenComparing(OrdersStatisticByMonthResponse::getMonth)
        );
        return ordersStatisticList;
    }
    private List<Order> getOrdersForPeriod(String startDate, String endDate) {
        return orderRepository.findAllByDateOfOrderBetween(
                LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                LocalDate.parse(endDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        );
    }

    private OrdersStatisticByMonthResponse mapToOrderStatisticByMonth(int year,
                                                                      Month month,
                                                                      OrdersStatisticDetails details) {
        BigDecimal avgOrderPrice = details.getSumOrderPrice()
                .divide(BigDecimal.valueOf(details.getCountOfOrders()), 2, RoundingMode.HALF_UP);
        return OrdersStatisticByMonthResponse.builder()
                .year(year)
                .month(month)
                .countOfOrders(details.getCountOfOrders())
                .sumOrderPrice(details.getSumOrderPrice())
                .avgOrderPrice(avgOrderPrice)
                .build();
    }
}
