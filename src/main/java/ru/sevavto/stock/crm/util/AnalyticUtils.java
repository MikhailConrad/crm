package ru.sevavto.stock.crm.util;

import ru.sevavto.stock.crm.model.dto.analytic.details.OrganizationRatingDetails;
import ru.sevavto.stock.crm.model.dto.analytic.OrganizationRatingResponse;
import ru.sevavto.stock.crm.model.dto.analytic.ProductCategoryRatingResponse;
import ru.sevavto.stock.crm.model.dto.analytic.ProductRatingResponse;
import ru.sevavto.stock.crm.model.dto.analytic.details.ProductRatingDetails;
import ru.sevavto.stock.crm.model.entity.Order;
import ru.sevavto.stock.crm.model.entity.OrderPosition;
import ru.sevavto.stock.crm.model.entity.Organization;
import ru.sevavto.stock.crm.model.entity.Product;
import ru.sevavto.stock.crm.model.entity.ProductCategory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AnalyticUtils {

//    public <T extends Ratingable> Map<T, RatingDetails> getRating(List<Order> orders, Class<T> targetClass) {
//        Map<T, RatingDetails> map = new HashMap<>();
//
//        orders.forEach(order ->
//        );
//    }

    public static List<ProductRatingResponse> getProductsRatingFromOrders(List<Order> orders) {
        Map<Product, ProductRatingDetails> productAndRatingDetailsMap = new HashMap<>();
        orders.forEach(order -> order.getPositions()
                .forEach(position -> upsertRatingDetailsAtMap(
                        productAndRatingDetailsMap,
                        position,
                        position.getProduct()))
        );
        List<ProductRatingResponse> productRatingList = new LinkedList<>();
        productAndRatingDetailsMap.forEach((key, value) ->
                productRatingList.add(mapToProductRatingResponse(key, value)));
        return productRatingList;
    }

    public static List<ProductCategoryRatingResponse> getCategoriesRatingFromOrders(List<Order> orders) {
        Map<ProductCategory, ProductRatingDetails> categoryAndRatingDetailsMap = new HashMap<>();
        orders.forEach(order -> order.getPositions()
                .forEach(position -> upsertRatingDetailsAtMap(
                        categoryAndRatingDetailsMap,
                        position,
                        position.getProduct().getProductCategory()))
        );
        List<ProductCategoryRatingResponse> categoryRatingList = new LinkedList<>();
        categoryAndRatingDetailsMap.forEach((key, value) ->
                categoryRatingList.add(mapToProductCategoryRatingResponse(key, value)));
        return categoryRatingList;
    }

    public static List<OrganizationRatingResponse> getOrganizationsRatingFromOrders(List<Order> orders) {
        Map<Organization, OrganizationRatingDetails> organizationAndRatingDetailsMap = new HashMap<>();
        orders.forEach(order -> {
                Organization organization = order.getOrganizationManager().getOrganization();
                BigDecimal totalPrice = order.getTotalPrice();
                organizationAndRatingDetailsMap.compute(
                        organization, (key, ratingDetails) -> ratingDetails == null
                                        ? new OrganizationRatingDetails(1, totalPrice)
                                        : OrganizationRatingDetails.builder()
                                .countOfOrders(ratingDetails.getCountOfOrders() + 1)
                                .sumOrderPrice(ratingDetails.getSumOrderPrice().add(totalPrice))
                                .build()
                );
            }
        );
        List<OrganizationRatingResponse> organizationRatingList = new LinkedList<>();
        organizationAndRatingDetailsMap.forEach((key, value) ->
                organizationRatingList.add(mapToOrganizationRatingResponse(key, value)));
        return organizationRatingList;
    }

    private static <T> void upsertRatingDetailsAtMap(Map<T, ProductRatingDetails> mapToUpdate, OrderPosition position, T object) {
        int amountAtPosition = position.getAmount();
        BigDecimal totalPriceAtPosition = position.getPriceRecord().getPrice()
                .multiply(BigDecimal.valueOf(position.getAmount()));

        mapToUpdate.compute(
                object, (key, ratingDetails) -> ratingDetails == null
                        ? new ProductRatingDetails(amountAtPosition, totalPriceAtPosition)
                        : ProductRatingDetails.builder()
                                        .countOfSold(ratingDetails.getCountOfSold() + amountAtPosition)
                                        .salesProfit(ratingDetails.getSalesProfit().add(totalPriceAtPosition))
                                        .build()
        );
    }

    private static ProductRatingResponse mapToProductRatingResponse(Product product, ProductRatingDetails ratingDetails) {
        return ProductRatingResponse.builder()
                .productCode(product.getProductCode())
                .name(product.getName())
                .description(product.getDescription())
                .productCategory(product.getProductCategory().getName())
                .weight(product.getWeight())
                .countOfSold(ratingDetails.getCountOfSold())
                .salesProfit(ratingDetails.getSalesProfit())
                .rating(0)
                .build();
    }

    private static ProductCategoryRatingResponse mapToProductCategoryRatingResponse(ProductCategory productCategory,
                                                                                    ProductRatingDetails ratingDetails) {
        return ProductCategoryRatingResponse.builder()
                .name(productCategory.getName())
                .description(productCategory.getDescription())
                .countOfSold(ratingDetails.getCountOfSold())
                .salesProfit(ratingDetails.getSalesProfit())
                .build();
    }

    private static OrganizationRatingResponse mapToOrganizationRatingResponse(Organization organization,
                                                                              OrganizationRatingDetails ratingDetails) {
        BigDecimal avgOrderPrice = ratingDetails.getSumOrderPrice()
                .divide(BigDecimal.valueOf(ratingDetails.getCountOfOrders()), 2, RoundingMode.HALF_UP);
        return OrganizationRatingResponse.builder()
                .name(organization.getName())
                .countOfOrders(ratingDetails.getCountOfOrders())
                .sumOrderPrice(ratingDetails.getSumOrderPrice())
                .avgOrderPrice(avgOrderPrice)
                .rating(0)
                .build();
    }
}
