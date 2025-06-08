package ru.yandex.practicum.payment.service;

import ru.yandex.practicum.iteractionapi.dto.order.OrderDto;
import ru.yandex.practicum.iteractionapi.dto.payment.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {
    PaymentDto createPayment(OrderDto orderDto);
    BigDecimal calculateTotalCost(OrderDto orderDto);
    void successPayment(UUID orderId);
    BigDecimal calculateProductCost(OrderDto orderDto);
    void failedPayment(UUID orderId);
}