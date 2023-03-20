package ru.sevavto.stock.crm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationManagerResponse {

    private long id;
    private String lastname;
    private String firstname;
    private String patronymicname;
    private String email;
    private String phoneNumber;
    private String region;
}
