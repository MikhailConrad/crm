package ru.sevavto.stock.crm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationResponse {

    private long id;
    private String name;
    private String innCode;
    private String ogrnCode;
    private String legalAddress; //юридический адрес
    private String bankName;
    private String bankAccountNumber;
    private List<OrganizationManagerResponse> organizationManagers;
}
