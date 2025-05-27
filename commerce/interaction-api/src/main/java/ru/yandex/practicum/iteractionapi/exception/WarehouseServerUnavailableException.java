package ru.yandex.practicum.iteractionapi.exception;

public class WarehouseServerUnavailableException extends RuntimeException {
    public WarehouseServerUnavailableException(String message) {
        super(message);
    }
}