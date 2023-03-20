package ru.sevavto.stock.crm.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class UpsertProductRequest {

    @NotNull
    @Min(value = 4, message = "Артикул товара состоит минимум из 4 цифр")
    @Pattern(regexp = "^\\d+$")
    private String productCode;
    @NotBlank
    @Pattern(regexp = "^[А-Яа-я-\\d\\s]+$", message = "Использованы недопустимые символы")
    private String name;
    private String description;
    @NotBlank
    @Pattern(regexp = "^[А-Яа-я-\\d\\s]+$", message = "Использованы недопустимые символы")
    private String productCategory;
    private double weight;
    @PositiveOrZero(message = "Количество не может быть меньше 0")
    private int quantity;
    @Pattern(regexp = "\\d+\\.\\d{2}", message = "Формат цены: 0.00")
    private BigDecimal price;
}
