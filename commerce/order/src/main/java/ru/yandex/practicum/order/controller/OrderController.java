package ru.yandex.practicum.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.iteractionapi.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.iteractionapi.dto.order.OrderDto;
import ru.yandex.practicum.iteractionapi.dto.order.ProductReturnRequest;
import ru.yandex.practicum.order.service.OrderServiceImpl;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderServiceImpl orderService;

    @GetMapping
    public OrderDto getOrder(@RequestParam String username) {
        return orderService.getOrders(username);
    }

    @PutMapping
    public OrderDto saveOrder(@RequestBody CreateNewOrderRequest newOrderRequest) {
        return orderService.saveOrder(newOrderRequest);
    }

    @PostMapping("/return")
    public OrderDto returnOrder(ProductReturnRequest orderDto) {
        return orderService.returnOrder(orderDto);
    }

    @PostMapping("/payment")
    public OrderDto paymentOrder(@RequestBody UUID orderId) {
        return orderService.paymentOrder(orderId);
    }

    @PostMapping("/payment/failed")
    public OrderDto paymentFailedOrder(@RequestBody UUID orderId) {
        return orderService.paymentFailedOrder(orderId);
    }

    @PostMapping("/delivery")
    public OrderDto deliveryOrder(@RequestBody UUID orderId) {
        return orderService.deliveryOrder(orderId);
    }

    @PostMapping("/delivery/failed")
    public OrderDto deliveryFailedOrder(@RequestBody UUID orderId) {
        return orderService.deliveryFailedOrder(orderId);
    }

    @PostMapping("/completed")
    public OrderDto completedOrder(@RequestBody UUID orderId) {
        return orderService.completedOrder(orderId);
    }

    @PostMapping("/calculate/total")
    public OrderDto calculateOrder(@RequestBody UUID orderId) {
        return orderService.calculateOrder(orderId);
    }

    @PostMapping("/calculate/delivery")
    public OrderDto calculateOrderDelivery(@RequestBody UUID orderId) {
        return orderService.calculateOrderDelivery(orderId);
    }

    @PostMapping("/assembly")
    public OrderDto assembleOrder(@RequestBody UUID orderId) {
        return orderService.assembleOrder(orderId);
    }


    @PostMapping("/assembly/failed")
    public OrderDto assembleFailedOrder(@RequestBody UUID orderId) {
        return orderService.assembleFailedOrder(orderId);
    }
}