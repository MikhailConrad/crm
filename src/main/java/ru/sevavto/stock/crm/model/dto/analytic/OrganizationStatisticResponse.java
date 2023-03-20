package ru.sevavto.stock.crm.model.dto.analytic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationStatisticResponse {

    private String name;
    private List<ProductCategoryRatingResponse> popularCategories;
    private List<ProductRatingResponse> popularProducts;
    private int countOfOrders;
    private BigDecimal sumOrderPrice;
    private BigDecimal avgOrderPrice;
}
