package ru.sevavto.stock.crm.model.dto.analytic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductStatisticResponse {

    private String name;
    private String productCode;
    private String categoryName;
    private List<ProductPriceDynamicResponse> priceDynamic;
}
