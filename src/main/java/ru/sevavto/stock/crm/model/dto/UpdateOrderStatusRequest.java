package ru.sevavto.stock.crm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sevavto.stock.crm.model.entity.OrderStatus;
import ru.sevavto.stock.crm.util.annotation.EnumValidator;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderStatusRequest {

    private long id;
    @EnumValidator(enumClass = OrderStatus.class, message = "Неверно указан статус заказа")
    private String status;
}
