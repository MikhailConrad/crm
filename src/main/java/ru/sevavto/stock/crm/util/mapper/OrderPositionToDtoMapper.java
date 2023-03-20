package ru.sevavto.stock.crm.util.mapper;

import ru.sevavto.stock.crm.model.dto.OrderPositionResponse;
import ru.sevavto.stock.crm.model.entity.OrderPosition;


public class OrderPositionToDtoMapper {
    public static OrderPositionResponse mapToDto(OrderPosition position) {
        OrderPositionResponse positionDto = OrderPositionResponse.builder()
                .id(position.getId())
                .product(ProductToDtoMapper.mapToDto(position.getProduct(), position.getPriceRecord().getPrice()))
                .amount(position.getAmount())
                .build();
        return positionDto;
    }
}
