package ru.sevavto.stock.crm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sevavto.stock.crm.model.entity.DefectiveProductRemoteStatementStatus;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DefectiveProductRemoteStatementResponse {
    private Long id;
    private OrganizationManagerResponse organizationManager;
    private OrderResponse order;
    private ProductResponse product;
    private int amount;
    private LocalDate dateOfApplication;
    private String comment;
    private List<DefectiveProductPhotoLinkResponse> photos;
    private DefectiveProductRemoteStatementStatus status;
    private String conclusion;
    private LocalDate dateOfReport;
}
