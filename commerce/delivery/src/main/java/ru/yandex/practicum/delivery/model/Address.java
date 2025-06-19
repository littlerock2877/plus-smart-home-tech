package ru.yandex.practicum.delivery.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "delivery_address", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String country;
    private String city;
    private String street;
    private String house;
    private String flat;

    @OneToOne(mappedBy = "fromAddress")
    private Delivery deliveryFrom;

    @OneToOne(mappedBy = "toAddress")
    private Delivery deliveryTo;
}