package ru.sevavto.stock.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sevavto.stock.crm.model.dto.analytic.ProductRatingResponse;
import ru.sevavto.stock.crm.model.dto.analytic.ProductStatisticResponse;
import ru.sevavto.stock.crm.service.ProductAnalyticService;

import java.util.List;

@RestController
@RequestMapping("/analytic/product")
public class ProductAnalyticController {

    private final ProductAnalyticService productAnalyticService;

    @Autowired
    public ProductAnalyticController(ProductAnalyticService productAnalyticService) {
        this.productAnalyticService = productAnalyticService;
    }

    @GetMapping(value = "statistic", params= {"productCode", "startDate", "endDate"})
    public ProductStatisticResponse getProductStatistic(@RequestParam String productCode,
                                                        @RequestParam String startDate,
                                                        @RequestParam String endDate) {
        return productAnalyticService.getProductStatistic(productCode, startDate, endDate);
    }

    //на фронте: фильтр по категории товаров,
    //сортировка по основному рейтингу, сортировка по кол-ву проданных товаров, сортировка по прибыли от продажи наименования товара
    @GetMapping(value = "rating", params = {"startDate", "endDate"})
    public List<ProductRatingResponse> getProductsRating(@RequestParam String startDate,
                                                         @RequestParam String endDate) {
        return productAnalyticService.getProductsRatingList(startDate, endDate);
    }

    //todo: антирейтинг товаров пропорционально количеству брака.
}
