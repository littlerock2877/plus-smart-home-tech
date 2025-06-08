package ru.yandex.practicum.payment.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.iteractionapi.dto.payment.PaymentDto;
import ru.yandex.practicum.payment.model.Payment;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface PaymentMapper {
    PaymentDto toDto(Payment payment);
}