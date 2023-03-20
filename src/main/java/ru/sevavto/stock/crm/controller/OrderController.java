package ru.sevavto.stock.crm.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sevavto.stock.crm.model.dto.UpdateOrderRequest;
import ru.sevavto.stock.crm.model.dto.OrderResponse;
import ru.sevavto.stock.crm.model.dto.UpdateOrderStatusRequest;
import ru.sevavto.stock.crm.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('order:read')")
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('order:read')")
    public OrderResponse getOrderById(@PathVariable long id) {
        return orderService.getOrderById(id);
    }

    @GetMapping(params = "organizationName")
    @PreAuthorize("hasAuthority('order:read')")
    public List<OrderResponse> getAllOrdersByOrganizationName(@RequestParam(value = "organizationName") String organizationName) {
        return orderService.getAllOrdersByOrganizationName(organizationName);
    }

    @GetMapping(params = "status")
    @PreAuthorize("hasAuthority('order:read')")
    public List<OrderResponse> getAllOrdersByStatus(@RequestParam(value = "status") String status) {
        return orderService.getAllOrdersByStatus(status);
    }

    @PutMapping("/status")
    @PreAuthorize("hasAuthority('order:upsert')")
    public OrderResponse changeOrderStatus(@RequestBody @Valid UpdateOrderStatusRequest request) {
        return orderService.changeOrderStatus(request.getId(), request.getStatus());
    }

    @PutMapping()
    @PreAuthorize("hasAuthority('order:upsert')")
    public OrderResponse updateOrder(@RequestBody UpdateOrderRequest request) {
        return orderService.updateOrder(request);
    }
}
