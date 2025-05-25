package ru.yandex.practicum.warehouse.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Builder
@Embeddable
public class Dimension {
    private Double width;
    private Double height;
    private Double depth;
}