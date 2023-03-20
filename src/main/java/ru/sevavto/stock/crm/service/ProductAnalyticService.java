package ru.sevavto.stock.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sevavto.stock.crm.exception.NotFoundException;
import ru.sevavto.stock.crm.model.dto.analytic.ProductPriceDynamicResponse;
import ru.sevavto.stock.crm.model.dto.analytic.ProductRatingResponse;
import ru.sevavto.stock.crm.model.dto.analytic.ProductStatisticResponse;
import ru.sevavto.stock.crm.model.entity.Order;
import ru.sevavto.stock.crm.model.entity.OrderPosition;
import ru.sevavto.stock.crm.model.entity.PriceRecord;
import ru.sevavto.stock.crm.model.entity.Product;
import ru.sevavto.stock.crm.repository.OrderRepository;
import ru.sevavto.stock.crm.repository.PriceRecordRepository;
import ru.sevavto.stock.crm.repository.ProductRepository;
import ru.sevavto.stock.crm.util.ComparatorByRating;
import ru.sevavto.stock.crm.util.AnalyticUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductAnalyticService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PriceRecordRepository priceRecordRepository;

    @Autowired
    public ProductAnalyticService(OrderRepository orderRepository,
                                  ProductRepository productRepository,
                                  PriceRecordRepository priceRecordRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.priceRecordRepository = priceRecordRepository;
    }

    public List<ProductRatingResponse> getProductsRatingList(String startDate, String endDate) {
        List<Order> ordersForPeriod = getOrdersForPeriod(startDate, endDate);
        List<ProductRatingResponse> productRatingList = AnalyticUtils.getProductsRatingFromOrders(ordersForPeriod);
        productRatingList.sort(new ComparatorByRating());
        return productRatingList;
    }

    public ProductStatisticResponse getProductStatistic(String productCode, String startDate, String endDate) {
        Product product = productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new NotFoundException("Продукт с таким кодом не найден в БД"));
        return ProductStatisticResponse.builder()
                .name(product.getName())
                .productCode(product.getProductCode())
                .categoryName(product.getProductCategory().getName())
                .priceDynamic(getProductPriceDynamic(product, startDate, endDate))
                .build();
    }

    private List<ProductPriceDynamicResponse> getProductPriceDynamic(Product product, String startDate, String endDate) {
        List<Order> ordersForPeriod = getOrdersForPeriod(startDate, endDate);
        List<PriceRecord> priceRecordsForProduct = getPriceRecordsForPeriod(product, startDate, endDate);
        Map<PriceRecord, Integer> priceAndCountOfSoldMap = priceRecordsForProduct.stream()
                .collect(Collectors.toMap(Function.identity(), i -> 0));

        for (Order order: ordersForPeriod) {
            for(OrderPosition position: order.getPositions()) {
                if(priceAndCountOfSoldMap.containsKey(position.getPriceRecord())) {
                    priceAndCountOfSoldMap.compute(position.getPriceRecord(), (key, value) -> value += position.getAmount());
                }
            }
        }
        List<ProductPriceDynamicResponse> pricesDynamic = new LinkedList<>();
        priceAndCountOfSoldMap.forEach((key, value) -> pricesDynamic.add(mapToPriceDynamicResponse(key, value)));
        Collections.sort(pricesDynamic);
        return pricesDynamic;
    }


    private List<PriceRecord> getPriceRecordsForPeriod(Product product, String startDate, String endDate) {
        return priceRecordRepository.findAllByProductAndDateOfPriceUpdateBetween(
                product,
                LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                LocalDate.parse(endDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        );
    }

    private List<Order> getOrdersForPeriod(String startDate, String endDate) {
        return orderRepository.findAllByDateOfOrderBetween(
                LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                LocalDate.parse(endDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        );
    }

    private ProductPriceDynamicResponse mapToPriceDynamicResponse(PriceRecord priceRecord, int countOfSold) {
        return ProductPriceDynamicResponse.builder()
                .dateOfPriceUpdate(priceRecord.getDateOfPriceUpdate())
                .price(priceRecord.getPrice())
                .countOfSold(countOfSold)
                .build();
    }
}
