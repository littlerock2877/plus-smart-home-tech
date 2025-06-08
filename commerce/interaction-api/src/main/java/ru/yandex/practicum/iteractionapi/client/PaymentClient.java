package ru.yandex.practicum.iteractionapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.iteractionapi.dto.order.OrderDto;
import ru.yandex.practicum.iteractionapi.dto.payment.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "payment", configuration = FeignConfig.class)
public interface PaymentClient {
    @PostMapping("/api/v1/payment")
    PaymentDto createPayment(@RequestBody OrderDto orderDto);

    @PostMapping("/api/v1/payment/totalCost")
    BigDecimal calculateTotalCost(@RequestBody OrderDto orderDto);

    @PostMapping("/api/v1/payment/refund")
    boolean successPayment(@RequestBody UUID orderId);

    @PostMapping("/api/v1/payment/productCost")
    BigDecimal calculateProductCost(@RequestBody OrderDto orderDto);

    @PostMapping("/api/v1/payment/failed")
    boolean failedPayment(@RequestBody UUID orderId);
}