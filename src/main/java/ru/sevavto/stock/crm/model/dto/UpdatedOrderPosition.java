package ru.sevavto.stock.crm.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatedOrderPosition {

    private long orderPositionId;
    @NotNull
    @Size(min = 4, message = "Артикул товара состоит минимум из 4 цифр")
    @Pattern(regexp = "^\\d+$", message = "Допустимо использование только цифр")
    private String productCode;
    @Positive(message = "Количество должно быть больше 0")
    private int amount;
}
