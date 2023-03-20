package ru.sevavto.stock.crm.model.dto.analytic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPriceDynamicResponse implements Comparable<ProductPriceDynamicResponse> {
    private LocalDate dateOfPriceUpdate;
    private BigDecimal price;
    private int countOfSold;

    @Override
    public int compareTo(ProductPriceDynamicResponse response) {
        if(this.dateOfPriceUpdate.isBefore(response.getDateOfPriceUpdate()))
            return -1;
        else if(this.dateOfPriceUpdate.isAfter(response.getDateOfPriceUpdate()))
            return 1;
        return 0;
    }
}
