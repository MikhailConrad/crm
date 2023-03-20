package ru.sevavto.stock.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sevavto.stock.crm.model.dto.analytic.ProductCategoryRatingResponse;
import ru.sevavto.stock.crm.model.entity.Order;
import ru.sevavto.stock.crm.repository.OrderRepository;
import ru.sevavto.stock.crm.util.AnalyticUtils;
import ru.sevavto.stock.crm.util.ComparatorByRating;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ProductCategoryAnalyticService {

    private final OrderRepository orderRepository;

    @Autowired
    public ProductCategoryAnalyticService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<ProductCategoryRatingResponse> getProductCategoriesRatingList(String startDate, String endDate) {
        List<Order> ordersForPeriod = getOrdersForPeriod(startDate, endDate);
        List<ProductCategoryRatingResponse> categoryRatingList = AnalyticUtils.getCategoriesRatingFromOrders(ordersForPeriod);
        categoryRatingList.sort(new ComparatorByRating());
        return categoryRatingList;
    }

    private List<Order> getOrdersForPeriod(String startDate, String endDate) {
        return orderRepository.findAllByDateOfOrderBetween(
                LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                LocalDate.parse(endDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        );
    }
}
