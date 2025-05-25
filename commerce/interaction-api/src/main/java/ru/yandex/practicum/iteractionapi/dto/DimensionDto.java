package ru.yandex.practicum.iteractionapi.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class DimensionDto {
    @Min(1)
    Double width;
    @Min(1)
    Double height;
    @Min(1)
    Double depth;
}