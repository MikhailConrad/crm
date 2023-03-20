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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sevavto.stock.crm.model.dto.UpsertOrganizationManagerRequest;
import ru.sevavto.stock.crm.model.dto.OrganizationManagerResponse;
import ru.sevavto.stock.crm.service.OrganizationManagerService;

import java.util.List;

@RestController
@RequestMapping("/organization_manager")
public class OrganizationManagerController {
    private final OrganizationManagerService organizationManagerService;

    @Autowired
    public OrganizationManagerController(OrganizationManagerService organizationManagerService) {
        this.organizationManagerService = organizationManagerService;
    }

    @GetMapping(params = "organizationName")
    @PreAuthorize("hasAuthority('organization_manager:read')")
    public List<OrganizationManagerResponse> getAllOrganizationManagersByOrganization(@RequestParam(value = "organizationName") String organizationName) {
        return organizationManagerService.getAllOrganizationManagersByOrganization(organizationName);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('organization_manager:read')")
    public OrganizationManagerResponse getOrganizationManagerById(@PathVariable long id) {
        return organizationManagerService.getOrganizationManagerById(id);
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('organization_manager:upsert')")
    public OrganizationManagerResponse addNewOrganizationManager(@RequestBody @Valid UpsertOrganizationManagerRequest request) {
        return organizationManagerService.addNewOrganizationManager(request);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('organization_manager:upsert')")
    public OrganizationManagerResponse updateOrganizationManager(@RequestBody @Valid UpsertOrganizationManagerRequest request) {
        return organizationManagerService.updateOrganizationManager(request);
    }
}
