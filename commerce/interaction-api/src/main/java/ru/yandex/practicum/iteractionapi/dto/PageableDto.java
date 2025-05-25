package ru.yandex.practicum.iteractionapi.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.List;

@Data
public class PageableDto {
    @Min(0)
    private Integer page;
    @Min(1)
    private Integer size;

    private List<String> sort;
}