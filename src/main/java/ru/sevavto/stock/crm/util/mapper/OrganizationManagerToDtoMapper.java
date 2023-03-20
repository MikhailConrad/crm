package ru.sevavto.stock.crm.util.mapper;

import ru.sevavto.stock.crm.model.dto.OrganizationManagerResponse;
import ru.sevavto.stock.crm.model.entity.OrganizationManager;

public class OrganizationManagerToDtoMapper {
    public static OrganizationManagerResponse mapToDto(OrganizationManager organizationManager) {
        OrganizationManagerResponse organizationManagerResponse = OrganizationManagerResponse.builder()
                .id(organizationManager.getId())
                .firstname(organizationManager.getFirstname())
                .lastname(organizationManager.getLastname())
                .patronymicname(organizationManager.getPatronymicname())
                .email(organizationManager.getEmail())
                .phoneNumber(organizationManager.getPhoneNumber())
                .region(organizationManager.getRegion())
                .build();
        return organizationManagerResponse;
    }
}