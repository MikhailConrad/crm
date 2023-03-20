package ru.sevavto.stock.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sevavto.stock.crm.model.dto.analytic.ProductCategoryRatingResponse;
import ru.sevavto.stock.crm.service.ProductCategoryAnalyticService;

import java.util.List;

@RestController
@RequestMapping("/analytic/category")
public class ProductCategoryAnalyticController {

    private final ProductCategoryAnalyticService productCategoryAnalyticService;

    @Autowired
    public ProductCategoryAnalyticController(ProductCategoryAnalyticService productCategoryAnalyticService) {
        this.productCategoryAnalyticService = productCategoryAnalyticService;
    }

    @GetMapping(value = "rating", params = {"startDate", "endDate"})
    public List<ProductCategoryRatingResponse> getProductCategoriesRating(@RequestParam String startDate,
                                                                          @RequestParam String endDate) {
        return productCategoryAnalyticService.getProductCategoriesRatingList(startDate, endDate);
    }
}
