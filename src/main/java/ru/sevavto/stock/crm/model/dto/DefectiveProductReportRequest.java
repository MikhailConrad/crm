package ru.sevavto.stock.crm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sevavto.stock.crm.model.entity.DefectiveProductRemoteStatementStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefectiveProductReportRequest {
    private long statementId;
    private DefectiveProductRemoteStatementStatus status;
    private String conclusion;
}
