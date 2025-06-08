package ru.yandex.practicum.order.service;

import ru.yandex.practicum.iteractionapi.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.iteractionapi.dto.order.OrderDto;
import ru.yandex.practicum.iteractionapi.dto.order.ProductReturnRequest;

import java.util.UUID;

public interface OrderService {

    OrderDto getOrders(String username);

    OrderDto saveOrder(CreateNewOrderRequest newOrderRequest);

    OrderDto returnOrder(ProductReturnRequest orderDto);

    OrderDto paymentOrder(UUID orderId);

    OrderDto paymentFailedOrder(UUID orderId);

    OrderDto deliveryOrder(UUID orderId);

    OrderDto deliveryFailedOrder(UUID orderId);

    OrderDto completedOrder(UUID orderId);

    OrderDto calculateOrder(UUID orderId);

    OrderDto calculateOrderDelivery(UUID orderId);

    OrderDto assembleOrder(UUID orderId);

    OrderDto assembleFailedOrder(UUID orderId);
}