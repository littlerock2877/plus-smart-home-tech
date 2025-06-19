package ru.yandex.practicum.delivery.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.delivery.model.Delivery;
import ru.yandex.practicum.iteractionapi.dto.delivery.DeliveryDto;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface DeliveryMapper {
    DeliveryDto toDto(Delivery delivery);
}