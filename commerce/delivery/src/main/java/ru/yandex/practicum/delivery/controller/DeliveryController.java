package ru.yandex.practicum.delivery.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.delivery.service.DeliveryService;
import ru.yandex.practicum.iteractionapi.dto.delivery.DeliveryDto;
import ru.yandex.practicum.iteractionapi.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;

    @PutMapping
    public DeliveryDto planDelivery(@RequestBody DeliveryDto deliveryDto) {
        return deliveryService.planDelivery(deliveryDto);
    }

    @PostMapping("/successful")
    public void successfulDelivery(@RequestBody UUID orderId) {
        deliveryService.successfulDelivery(orderId);
    }

    @PostMapping("/picked")
    public void pickedToDelivery(@RequestBody UUID orderId) {
        deliveryService.pickedToDelivery(orderId);
    }

    @PostMapping("/failed")
    public void failedDelivery(@RequestBody UUID orderId) {
        deliveryService.failedDelivery(orderId);
    }

    @PostMapping("/cost")
    public BigDecimal calculateFullCost(@RequestBody OrderDto deliveryDto) {
        return deliveryService.calculateFullDeliveryCost(deliveryDto);
    }
}