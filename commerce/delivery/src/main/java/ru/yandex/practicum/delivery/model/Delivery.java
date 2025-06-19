package ru.yandex.practicum.delivery.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.iteractionapi.dto.delivery.DeliveryState;

import java.util.UUID;

@Entity
@Table(name = "delivery", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID deliveryId;

    @OneToOne
    @JoinColumn(name = "delivery_from_id")
    private Address fromAddress;

    @OneToOne
    @JoinColumn(name = "delivery_to_id")
    private Address toAddress;

    private Double deliveryWeight;
    private Double deliveryVolume;
    private Boolean fragile;
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    private DeliveryState deliveryState;
}