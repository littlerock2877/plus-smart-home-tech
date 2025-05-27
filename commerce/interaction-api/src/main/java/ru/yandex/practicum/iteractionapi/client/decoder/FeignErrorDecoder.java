package ru.yandex.practicum.iteractionapi.client.decoder;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.iteractionapi.exception.DataValidationException;
import ru.yandex.practicum.iteractionapi.exception.EntityNotFoundException;
import ru.yandex.practicum.iteractionapi.exception.InternalServerErrorException;

@Component
public class FeignErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {
            case 400 -> new DataValidationException("Bad argument requests for method: %s".formatted(methodKey));
            case 404 -> new EntityNotFoundException("Resource not found for method: %s".formatted(methodKey));
            case 500 -> new InternalServerErrorException("Server error occurred in method: %s".formatted(methodKey));
            default -> defaultDecoder.decode(methodKey, response);
        };
    }
}