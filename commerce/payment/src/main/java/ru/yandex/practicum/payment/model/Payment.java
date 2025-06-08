package ru.yandex.practicum.payment.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.iteractionapi.dto.payment.PaymentStatus;

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

    private Double totalPrice;
    private Double deliveryPrice;
    private Double productPrice;
    @Enumerated(value = EnumType.STRING)
    private PaymentStatus status;
}