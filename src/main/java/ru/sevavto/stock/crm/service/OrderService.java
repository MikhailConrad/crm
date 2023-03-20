package ru.sevavto.stock.crm.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sevavto.stock.crm.exception.ExcessOfAvailableQuantityException;
import ru.sevavto.stock.crm.exception.NotFoundException;
import ru.sevavto.stock.crm.exception.StatusChangingException;
import ru.sevavto.stock.crm.model.dto.UpdateOrderRequest;
import ru.sevavto.stock.crm.model.dto.OrderResponse;
import ru.sevavto.stock.crm.model.dto.UpdatedOrderPosition;
import ru.sevavto.stock.crm.util.mapper.OrderToDtoMapper;
import ru.sevavto.stock.crm.model.entity.ActualProductPrice;
import ru.sevavto.stock.crm.model.entity.Order;
import ru.sevavto.stock.crm.model.entity.OrderPosition;
import ru.sevavto.stock.crm.model.entity.OrderStatus;
import ru.sevavto.stock.crm.model.entity.Product;
import ru.sevavto.stock.crm.repository.ActualProductPriceRepository;
import ru.sevavto.stock.crm.repository.OrderPositionRepository;
import ru.sevavto.stock.crm.repository.OrderRepository;
import ru.sevavto.stock.crm.repository.ProductRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderPositionRepository orderPositionRepository;
    private final ProductRepository productRepository;
    private final ActualProductPriceRepository actualProductPriceRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderPositionRepository orderPositionRepository, ProductRepository productRepository, ActualProductPriceRepository actualProductPriceRepository) {
        this.orderRepository = orderRepository;
        this.orderPositionRepository = orderPositionRepository;
        this.productRepository = productRepository;
        this.actualProductPriceRepository = actualProductPriceRepository;
    }

    public OrderResponse getOrderById(long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заказа с таким id не существует"));
        return OrderToDtoMapper.mapToDto(order);
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderToDtoMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getAllOrdersByOrganizationName(String organizationName) {
        List<Order> orders = orderRepository.findAllByOrganizationManager_OrganizationName(organizationName);
        return orders.stream()
                .map(OrderToDtoMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getAllOrdersByStatus(String status) {
        List<Order> orders = orderRepository.findAllByStatus(OrderStatus.valueOf(status));
        return orders.stream()
                .map(OrderToDtoMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(long orderId, String updatedStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Заказа с таким id не существует"));
        OrderStatus newStatus = OrderStatus.valueOf(updatedStatus);
        if(order.getStatus().equals(OrderStatus.CANCELED) || order.getStatus().equals(OrderStatus.COMPLETED)) {
            throw new StatusChangingException("Нельзя изменить статус. Заказ был отменен или уже выполнен.");
        }
        if(newStatus.equals(OrderStatus.APPROVED)
                && !order.getStatus().equals(OrderStatus.NEW)) {
            throw new StatusChangingException("Нельзя изменить статус на Подвержден, так как заказ не является новым.");
        }
        if(newStatus.equals(OrderStatus.PAID)
                && !order.getStatus().equals(OrderStatus.APPROVED)) {
            throw new StatusChangingException("Нельзя изменить статус на Оплачен, так как заказ не подтвержден.");
        }
        if(newStatus.equals(OrderStatus.COMPLETED)
                && !order.getStatus().equals(OrderStatus.COLLECTED)) {
            throw new StatusChangingException("Нельзя изменить статус на Выполнен, так как заказ не собран.");
        }
        if (newStatus.equals(OrderStatus.CANCELED)) {
            if(order.getStatus().equals(OrderStatus.PAID) || order.getStatus().equals(OrderStatus.COLLECTED)) {
                //todo: вернуть деньги клиенту
            }
            orderPositionRepository.findAllByOrderId(orderId)
                    .forEach(position -> updateProductQuantity(position.getProduct(), position.getAmount()));
        }
        order.setStatus(newStatus);
        return OrderToDtoMapper.mapToDto(orderRepository.save(order));
    }

    @Transactional
    public OrderResponse updateOrder(UpdateOrderRequest request) {
        Order orderToUpdate = orderRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Заказа с таким id не существует"));

        Set<Long> updatedPositionsIdSet = request.getPositions().stream()
                .map(UpdatedOrderPosition::getOrderPositionId)
                .collect(Collectors.toSet());
        List<OrderPosition> positionsToDelete = orderToUpdate.getPositions().stream()
                .filter(position -> !updatedPositionsIdSet.contains(position.getId()))
                .peek(position -> updateProductQuantity(position.getProduct(), position.getAmount()))
                .collect(Collectors.toList());
        orderToUpdate.getPositions().removeAll(positionsToDelete);
        orderPositionRepository.deleteAll(positionsToDelete);

        Map<Long, OrderPosition> positionsAtDbMap = orderToUpdate.getPositions().stream()
                .collect(Collectors.toMap(OrderPosition::getId, Function.identity()));
        for (UpdatedOrderPosition updatedPosition : request.getPositions()) {
            OrderPosition positionAtDb = positionsAtDbMap.get(updatedPosition.getOrderPositionId());
            if (positionAtDb != null && positionAtDb.getAmount() != updatedPosition.getAmount()) {
                updateProductQuantity(positionAtDb.getProduct(), positionAtDb.getAmount() - updatedPosition.getAmount());
                positionAtDb.setAmount(updatedPosition.getAmount()); // работает по ссылке на position в orderToUpdate
            } else if (positionAtDb == null) {
                Product product = productRepository.findByProductCode(updatedPosition.getProductCode())
                        .orElseThrow(() -> new NotFoundException("Продукта с таким id не существует"));
                ActualProductPrice actualProductPrice = actualProductPriceRepository.findByProduct(product);
                OrderPosition newOrderPosition = OrderPosition.builder()
                        .order(orderToUpdate)
                        .product(product)
                        .priceRecord(actualProductPrice.getPriceRecord())
                        .amount(updatedPosition.getAmount())
                        .build();
                orderToUpdate.getPositions().add(newOrderPosition);
                updateProductQuantity(product, - newOrderPosition.getAmount());
            }
        }
        BigDecimal newTotalPrice = orderToUpdate.getPositions().stream()
                .map(p -> p.getPriceRecord().getPrice().multiply(BigDecimal.valueOf(p.getAmount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        orderToUpdate.setTotalPrice(newTotalPrice.setScale(2, RoundingMode.HALF_UP));
        orderToUpdate.setDeliveryAddress(request.getDeliveryAddress());

        Order orderBeforeUpdate = orderRepository.save(orderToUpdate);
        return OrderToDtoMapper.mapToDto(orderBeforeUpdate);
    }

    private void updateProductQuantity(Product product, int difference) {
        int currentQuantityAtStock = product.getQuantity();
        int quantityToSave = currentQuantityAtStock + difference;
        if(quantityToSave < 0) {
            throw new ExcessOfAvailableQuantityException("Количество товара (" + product.getName()
                    + ") в заказе превышает доступное на складе: " + currentQuantityAtStock);
        }
        product.setQuantity(quantityToSave);
        productRepository.save(product);
    }
}
