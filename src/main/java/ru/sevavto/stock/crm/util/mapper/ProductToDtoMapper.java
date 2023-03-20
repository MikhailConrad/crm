package ru.sevavto.stock.crm.util.mapper;

import ru.sevavto.stock.crm.model.dto.ProductResponse;
import ru.sevavto.stock.crm.model.entity.Product;

import java.math.BigDecimal;

public class ProductToDtoMapper {
    public static ProductResponse mapToDto(Product product, BigDecimal price) {
        ProductResponse productResponse = ProductResponse.builder()
                .productCode(product.getProductCode())
                .name(product.getName())
                .description(product.getDescription())
                .productCategory(product.getProductCategory().getName())
                .weight(product.getWeight())
                .quantity(product.getQuantity())
                .price(price)
                .build();
        return productResponse;
    }
}
