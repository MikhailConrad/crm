package ru.sevavto.stock.crm.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sevavto.stock.crm.model.dto.OrderResponse;
import ru.sevavto.stock.crm.model.dto.OrganizationManagerResponse;
import ru.sevavto.stock.crm.model.dto.OrganizationResponse;
import ru.sevavto.stock.crm.model.dto.UpdateOrderRequest;
import ru.sevavto.stock.crm.model.dto.UpsertOrganizationManagerRequest;
import ru.sevavto.stock.crm.model.dto.UpsertOrganizationRequest;
import ru.sevavto.stock.crm.service.OrganizationService;

import java.util.List;

@RestController
@RequestMapping("/organization")
public class OrganizationController {
    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('organization:read')")
    public List<OrganizationResponse> getAllOrganizations() {
        return organizationService.getAllOrganizations();
    }

    @GetMapping({"/{id}"})
    @PreAuthorize("hasAuthority('organization:read')")
    public OrganizationResponse getOrganizationById(@PathVariable long id) {
        return organizationService.getOrganizationById(id);
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('organization:upsert')")
    public OrganizationResponse addNewOrganization(@RequestBody @Valid UpsertOrganizationRequest request) {
        return organizationService.addNewOrganization(request);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('organization:upsert')")
    public OrganizationResponse updateOrganizationManager(@RequestBody @Valid UpsertOrganizationRequest request) {
        return organizationService.updateOrganization(request);
    }
}
