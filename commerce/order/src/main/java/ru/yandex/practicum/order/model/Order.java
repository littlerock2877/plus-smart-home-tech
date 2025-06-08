package ru.yandex.practicum.order.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.iteractionapi.dto.order.OrderState;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "order", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;
    @OneToMany(mappedBy = "order")
    private List<OrderProduct> products;
    private UUID paymentId;
    private UUID shoppingCartId;
    private UUID deliveryId;
    @Enumerated(EnumType.STRING)
    private OrderState state;
    private Double deliveryWeight;
    private Double deliveryVolume;
    private Boolean fragile;
    private Double totalPrice;
    private Double deliveryPrice;
    private Double productPrice;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Address deliveryAddress;
}