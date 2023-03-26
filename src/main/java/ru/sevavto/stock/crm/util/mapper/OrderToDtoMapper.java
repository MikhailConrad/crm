package ru.sevavto.stock.crm.util.mapper;

import ru.sevavto.stock.crm.model.dto.OrderResponse;
import ru.sevavto.stock.crm.model.entity.Order;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class OrderToDtoMapper {
    public static OrderResponse mapToDto(Order order) {
        OrderResponse orderResponse = OrderResponse.builder()
                .id(order.getId())
                .organization(order.getOrganizationManager().getOrganization().getName())
                .organizationManager(OrganizationManagerToDtoMapper.mapToDto(order.getOrganizationManager()))
                .positions(order.getPositions().stream()
                                .map(OrderPositionToDtoMapper::mapToDto)
                                .collect(Collectors.toList()))
                .totalPrice(order.getTotalPrice())
                .dateOfOrder(order.getDateOfOrder().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .dateOfStatusUpdate(order.getDateOfStatusUpdate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .status(order.getStatus())
                .deliveryAddress(order.getDeliveryAddress())
                .build();
        return orderResponse;
    }
}
