package ru.sevavto.stock.crm.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sevavto.stock.crm.model.dto.analytic.OrganizationRatingResponse;
import ru.sevavto.stock.crm.model.dto.analytic.OrganizationStatisticResponse;
import ru.sevavto.stock.crm.service.OrganizationAnalyticService;

import java.util.List;

@RestController
@RequestMapping("analytic/organization")
public class OrganizationAnalyticController {

    private final OrganizationAnalyticService organizationAnalyticService;

    public OrganizationAnalyticController(OrganizationAnalyticService organizationAnalyticService) {
        this.organizationAnalyticService = organizationAnalyticService;
    }

    @GetMapping(value = "statistic", params = {"name", "startDate", "endDate"})
    public OrganizationStatisticResponse getOrganizationStatistic(@RequestParam String name,
                                                                  @RequestParam String startDate,
                                                                  @RequestParam String endDate) {
        return organizationAnalyticService.getOrganizationStatistic(name, startDate, endDate);
    }

    @GetMapping(value = "rating", params = {"startDate", "endDate"})
    public List<OrganizationRatingResponse> getOrganizationsRating(@RequestParam String startDate,
                                                                   @RequestParam String endDate) {
        return organizationAnalyticService.getOrganizationsRatingList(startDate, endDate);
    }
}
