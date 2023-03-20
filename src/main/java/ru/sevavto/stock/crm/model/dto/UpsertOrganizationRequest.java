package ru.sevavto.stock.crm.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpsertOrganizationRequest {

    private long id;
    @NotNull
    @Pattern(regexp = "^[А-Яа-я-\\d\\s]+$", message = "Использованы недопустимые символы")
    private String name;
    @NotNull
    @Pattern(regexp = "^\\d+$")
    private String innCode;
    @NotNull
    @Pattern(regexp = "^\\d+$")
    private String ogrnCode;
    @NotBlank
    private String legalAddress; //юридический адрес
    @NotNull
    @Pattern(regexp = "^[А-Яа-я-\\d\\s]+$", message = "Использованы недопустимые символы")
    private String bankName;
    @NotNull
    @Pattern(regexp = "^\\d+$")
    private String bankAccountNumber;
}
