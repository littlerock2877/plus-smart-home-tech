package ru.yandex.practicum.order.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "order_address", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {
    @Id
    @Column(name = "order_id")
    private UUID orderId;
    private String country;
    private String city;
    private String street;
    private String house;
    private String flat;
    @OneToOne
    @MapsId
    @JoinColumn(name = "order_id")
    private Order order;
}