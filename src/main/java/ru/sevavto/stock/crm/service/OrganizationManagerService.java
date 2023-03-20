package ru.sevavto.stock.crm.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sevavto.stock.crm.exception.NotFoundException;
import ru.sevavto.stock.crm.model.dto.OrganizationManagerResponse;
import ru.sevavto.stock.crm.model.dto.UpsertOrganizationManagerRequest;
import ru.sevavto.stock.crm.util.mapper.OrganizationManagerToDtoMapper;
import ru.sevavto.stock.crm.model.entity.OrganizationManager;
import ru.sevavto.stock.crm.repository.OrganizationManagerRepository;
import ru.sevavto.stock.crm.repository.OrganizationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganizationManagerService {

    private final OrganizationManagerRepository organizationManagerRepository;
    private final OrganizationRepository organizationRepository;

    @Autowired
    public OrganizationManagerService(OrganizationManagerRepository organizationManagerRepository, OrganizationRepository organizationRepository) {
        this.organizationManagerRepository = organizationManagerRepository;
        this.organizationRepository = organizationRepository;
    }

    public List<OrganizationManagerResponse> getAllOrganizationManagersByOrganization(String organizationName) {
        List<OrganizationManager> organizationManagers = organizationManagerRepository.findByOrganizationName(organizationName);
        return organizationManagers.stream()
                .map(OrganizationManagerToDtoMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public OrganizationManagerResponse getOrganizationManagerById(long id) {
        OrganizationManager organizationManager = organizationManagerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Менеджера компании с таким id не существует"));
        return OrganizationManagerToDtoMapper.mapToDto(organizationManager);
    }

    @Transactional
    public OrganizationManagerResponse addNewOrganizationManager(UpsertOrganizationManagerRequest request) {
        OrganizationManager organizationManager = OrganizationManager.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .patronymicname(request.getPatronymicname())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .region(request.getRegion())
                .password(request.getPassword()) //encoder
                .organization(organizationRepository.findByName(request.getOrganizationName())
                        .orElseThrow(() -> new NotFoundException("Организация с таким названием не найдена")))
                .build();
        return OrganizationManagerToDtoMapper.mapToDto(organizationManagerRepository.save(organizationManager));
    }

    @Transactional
    public OrganizationManagerResponse updateOrganizationManager(UpsertOrganizationManagerRequest request) {
        OrganizationManager organizationManager = organizationManagerRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Менеджера компании с таким id не существует"));

        organizationManager.setFirstname(request.getFirstname());
        organizationManager.setLastname(request.getLastname());
        organizationManager.setPatronymicname(request.getPatronymicname());
        organizationManager.setEmail(request.getEmail());
        organizationManager.setPhoneNumber(request.getPhoneNumber());
        organizationManager.setRegion(request.getRegion());
        organizationManager.setPassword(request.getPassword());
        return OrganizationManagerToDtoMapper.mapToDto(organizationManagerRepository.save(organizationManager));
    }
}
