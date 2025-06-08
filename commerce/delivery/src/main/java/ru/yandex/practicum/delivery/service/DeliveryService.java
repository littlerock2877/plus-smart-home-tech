package ru.yandex.practicum.delivery.service;


import ru.yandex.practicum.iteractionapi.dto.delivery.DeliveryDto;
import ru.yandex.practicum.iteractionapi.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryService {
    DeliveryDto planDelivery(DeliveryDto deliveryDto);

    void successfulDelivery(UUID orderId);

    void pickedToDelivery(UUID orderId);

    void failedDelivery(UUID orderId);

    BigDecimal calculateFullDeliveryCost(OrderDto orderDto);
}