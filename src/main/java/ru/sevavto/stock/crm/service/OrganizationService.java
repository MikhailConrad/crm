package ru.sevavto.stock.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sevavto.stock.crm.exception.NotFoundException;
import ru.sevavto.stock.crm.model.dto.OrganizationResponse;
import ru.sevavto.stock.crm.model.dto.UpsertOrganizationRequest;
import ru.sevavto.stock.crm.model.entity.Organization;
import ru.sevavto.stock.crm.repository.OrganizationRepository;
import ru.sevavto.stock.crm.util.mapper.OrganizationToDtoMapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public List<OrganizationResponse> getAllOrganizations() {
        return organizationRepository.findAll().stream()
                .map(OrganizationToDtoMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public OrganizationResponse getOrganizationById(long organizationId) {
        return OrganizationToDtoMapper.mapToDto(organizationRepository.findById(organizationId)
                .orElseThrow(() -> new NotFoundException("Организации с таким id не существует")));
    }


    public OrganizationResponse addNewOrganization(UpsertOrganizationRequest request) {
        Organization organization = Organization.builder()
                .name(request.getName())
                .innCode(request.getInnCode())
                .ogrnCode(request.getOgrnCode())
                .legalAddress(request.getLegalAddress())
                .bankName(request.getBankName())
                .bankAccountNumber(request.getBankAccountNumber())
                .organizationManagers(Collections.emptyList())
                .build();
        return OrganizationToDtoMapper.mapToDto(organizationRepository.save(organization));
    }

    public OrganizationResponse updateOrganization(UpsertOrganizationRequest request) {
        Organization organization = organizationRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Организации с таким id не существует"));

        organization.setName(request.getName());
        organization.setInnCode(request.getInnCode());
        organization.setOgrnCode(request.getOgrnCode());
        organization.setLegalAddress(request.getLegalAddress());
        organization.setBankName(request.getBankName());
        organization.setBankAccountNumber(request.getBankAccountNumber());
        return OrganizationToDtoMapper.mapToDto(organizationRepository.save(organization));
    }
}
