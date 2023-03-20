package ru.sevavto.stock.crm.util.mapper;

import ru.sevavto.stock.crm.model.dto.OrganizationResponse;
import ru.sevavto.stock.crm.model.entity.Organization;

import java.util.stream.Collectors;

public class OrganizationToDtoMapper {

    public static OrganizationResponse mapToDto(Organization organization) {
        OrganizationResponse organizationResponse = OrganizationResponse.builder()
                .id(organization.getId())
                .name(organization.getName())
                .innCode(organization.getInnCode())
                .ogrnCode(organization.getOgrnCode())
                .legalAddress(organization.getLegalAddress())
                .bankName(organization.getBankName())
                .bankAccountNumber(organization.getBankAccountNumber())
                .organizationManagers(organization.getOrganizationManagers().stream()
                        .map(OrganizationManagerToDtoMapper::mapToDto)
                        .collect(Collectors.toList()))
                .build();
        return organizationResponse;
    }
}
