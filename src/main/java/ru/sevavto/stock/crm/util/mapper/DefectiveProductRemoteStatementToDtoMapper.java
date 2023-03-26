package ru.sevavto.stock.crm.util.mapper;

import ru.sevavto.stock.crm.model.dto.DefectiveProductRemoteStatementResponse;
import ru.sevavto.stock.crm.model.entity.DefectiveProductRemoteStatement;

import java.math.BigDecimal;

public class DefectiveProductRemoteStatementToDtoMapper {

    public static DefectiveProductRemoteStatementResponse mapToDto(DefectiveProductRemoteStatement statement) {
        return DefectiveProductRemoteStatementResponse.builder()
                .id(statement.getId())
                .organizationManager(OrganizationManagerToDtoMapper.mapToDto(statement.getOrganizationManager()))
                .order(OrderToDtoMapper.mapToDto(statement.getOrder()))
                .product(ProductToDtoMapper.mapToDto(statement.getProduct(),
                        statement.getOrder().getPositions().stream()
                                .filter(position -> position.getProduct().equals(statement.getProduct()))
                                .map(position -> position.getPriceRecord().getPrice())
                                .findFirst().orElse(BigDecimal.ZERO)))
                .amount(statement.getAmount())
                .dateOfApplication(statement.getDateOfApplication())
                .comment(statement.getComment())
                .photos(statement.getPhotos().stream()
                        .map(DefectiveProductPhotoLinkToDtoMapper::mapToDto)
                        .toList())
                .status(statement.getStatus())
                .dateOfReport(statement.getDateOfReport())
                .conclusion(statement.getConclusion())
                .build();
    }
}
