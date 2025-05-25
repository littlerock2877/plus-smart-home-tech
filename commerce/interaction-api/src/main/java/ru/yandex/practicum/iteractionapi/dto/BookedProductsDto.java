package ru.yandex.practicum.iteractionapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookedProductsDto {
    @NotNull
    private Double deliveryWeight;
    @NotNull
    private Double deliveryVolume;
    @NotNull
    private Boolean fragile;
}