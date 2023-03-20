package ru.sevavto.stock.crm.model.dto.analytic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProductRatingResponse extends RatingSellableObject {

    private String productCode;
    private String name;
    private String description;
    private String productCategory;
    private double weight;
}
