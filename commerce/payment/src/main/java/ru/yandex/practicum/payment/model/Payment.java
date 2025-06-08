package ru.yandex.practicum.payment.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.iteractionapi.dto.payment.PaymentStatus;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID paymentId;
    private UUID orderId;

    private BigDecimal totalPrice;
    private BigDecimal deliveryPrice;
    private BigDecimal productPrice;
    @Enumerated(value = EnumType.STRING)
    private PaymentStatus status;
}