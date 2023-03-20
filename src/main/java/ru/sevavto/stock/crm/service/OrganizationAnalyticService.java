package ru.sevavto.stock.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sevavto.stock.crm.model.dto.analytic.OrganizationRatingResponse;
import ru.sevavto.stock.crm.model.dto.analytic.OrganizationStatisticResponse;
import ru.sevavto.stock.crm.model.dto.analytic.ProductCategoryRatingResponse;
import ru.sevavto.stock.crm.model.dto.analytic.ProductRatingResponse;
import ru.sevavto.stock.crm.model.entity.Order;
import ru.sevavto.stock.crm.repository.OrderRepository;
import ru.sevavto.stock.crm.util.AnalyticUtils;
import ru.sevavto.stock.crm.util.ComparatorByRating;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
public class OrganizationAnalyticService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrganizationAnalyticService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrganizationStatisticResponse getOrganizationStatistic(String name, String startDate, String endDate) {
        List<Order> organizationOrders = getOrdersForPeriod(startDate, endDate).stream()
                .filter(order -> order.getOrganizationManager().getOrganization().getName().equals(name))
                .toList();
        BigDecimal sumOrderPrice = calculateSumOrderPrice(organizationOrders);
        BigDecimal avgOrderPrice = calculateAvgOrderPrice(sumOrderPrice, organizationOrders.size());
        List<ProductCategoryRatingResponse> popularCategories = AnalyticUtils.getCategoriesRatingFromOrders(organizationOrders).stream()
                .sorted(Comparator.comparing(ProductCategoryRatingResponse::getCountOfSold).reversed())
                .limit(3)
                .toList();
        List<ProductRatingResponse> popularProducts = AnalyticUtils.getProductsRatingFromOrders(organizationOrders).stream()
                .sorted(Comparator.comparing(ProductRatingResponse::getCountOfSold))
                .limit(5)
                .toList();
        return OrganizationStatisticResponse.builder()
                .name(name)
                .countOfOrders(organizationOrders.size())
                .popularCategories(popularCategories)
                .popularProducts(popularProducts)
                .sumOrderPrice(sumOrderPrice)
                .avgOrderPrice(avgOrderPrice)
                .build();
    }

    public List<OrganizationRatingResponse> getOrganizationsRatingList(String startDate, String endDate) {
        List<Order> ordersForPeriod = getOrdersForPeriod(startDate, endDate);
        List<OrganizationRatingResponse> organizationRatingList =
                AnalyticUtils.getOrganizationsRatingFromOrders(ordersForPeriod);
        organizationRatingList.sort(new ComparatorByRating());
        return organizationRatingList;
    }

    private BigDecimal calculateSumOrderPrice(List<Order> orders) {
        return orders.stream()
                .map(Order::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateAvgOrderPrice(BigDecimal sumOrderPrice, int ordersCount) {
        return sumOrderPrice.divide(BigDecimal.valueOf(ordersCount), 2, RoundingMode.HALF_UP);
    }

    private List<Order> getOrdersForPeriod(String startDate, String endDate) {
        return orderRepository.findAllByDateOfOrderBetween(
                LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                LocalDate.parse(endDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        );
    }
}
