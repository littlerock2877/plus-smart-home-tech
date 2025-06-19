package ru.yandex.practicum.iteractionapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.iteractionapi.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.iteractionapi.dto.order.OrderDto;
import ru.yandex.practicum.iteractionapi.dto.order.ProductReturnRequest;

import java.util.UUID;

@FeignClient(name = "order", configuration = FeignConfig.class)
public interface OrderClient {
    @PutMapping
    OrderDto saveOrder(CreateNewOrderRequest newOrderRequest);

    @PostMapping("/return")
    OrderDto returnOrder(ProductReturnRequest orderDto);

    @PostMapping("/payment")
    OrderDto paymentOrder(UUID orderId);

    @PostMapping("/payment/failed")
    OrderDto paymentFailedOrder(UUID orderId);

    @PostMapping("/delivery")
    OrderDto deliveryOrder(UUID orderId);

    @PostMapping("/delivery/failed")
    OrderDto deliveryFailedOrder(UUID orderId);

    @PostMapping("/completed")
    OrderDto completedOrder(UUID orderId);

    @PostMapping("/calculate/total")
    OrderDto calculateOrder(UUID orderId);

    @PostMapping("/calculate/delivery")
    OrderDto calculateOrderDelivery(UUID orderId);

    @PostMapping("/assembly")
    OrderDto assembleOrder(UUID orderId);

    @PostMapping("/assembly/failed")
    OrderDto assembleFailedOrder(UUID orderId);
}