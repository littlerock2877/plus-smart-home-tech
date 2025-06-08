package ru.yandex.practicum.order.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.iteractionapi.dto.AddressDto;
import ru.yandex.practicum.order.model.Address;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface AddressMapper {
    Address toOrderAddress(AddressDto addressDto);
}