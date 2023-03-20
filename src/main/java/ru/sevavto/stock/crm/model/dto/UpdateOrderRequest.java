package ru.sevavto.stock.crm.model.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderRequest {

    private long id;
    @NotNull
    @Size(min = 1, message = "Список позиций заказа не может быть пустым")
    private List<UpdatedOrderPosition> positions;
    @NotBlank
    private String deliveryAddress;
}
