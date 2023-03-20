package ru.sevavto.stock.crm.model.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sevavto.stock.crm.model.entity.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private long id;
    private String organization;
    private OrganizationManagerResponse organizationManager;
    private List<OrderPositionResponse> positions;
    private BigDecimal totalPrice;
    private String dateOfOrder;
    private String dateOfStatusUpdate;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private String deliveryAddress;
}
