package ru.yandex.practicum.delivery.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.delivery.model.Address;
import ru.yandex.practicum.iteractionapi.dto.AddressDto;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface AddressMapper {
    Address toAddress(AddressDto addressDto);
}