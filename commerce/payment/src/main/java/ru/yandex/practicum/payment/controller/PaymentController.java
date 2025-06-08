package ru.yandex.practicum.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.iteractionapi.dto.order.OrderDto;
import ru.yandex.practicum.iteractionapi.dto.payment.PaymentDto;
import ru.yandex.practicum.payment.service.PaymentService;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public PaymentDto createPayment(@RequestBody OrderDto orderDto) {
        return paymentService.createPayment(orderDto);
    }

    @PostMapping("/totalCost")
    public BigDecimal calculateTotalCost(@RequestBody OrderDto orderDto) {
        return paymentService.calculateTotalCost(orderDto);
    }

    @PostMapping("/refund")
    public void successPayment(@RequestBody UUID orderId) {
        paymentService.successPayment(orderId);
    }

    @PostMapping("/productCost")
    public BigDecimal calculateProductCost(@RequestBody OrderDto orderDto) {
        return paymentService.calculateProductCost(orderDto);
    }

    @PostMapping("/failed")
    public void failedPayment(@RequestBody UUID orderId) {
        paymentService.failedPayment(orderId);
    }
}