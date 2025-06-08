package ru.yandex.practicum.iteractionapi.circuitbreaker;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.iteractionapi.dto.AddressDto;
import ru.yandex.practicum.iteractionapi.dto.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.iteractionapi.dto.BookedProductsDto;
import ru.yandex.practicum.iteractionapi.dto.ShippedToDeliveryRequest;
import ru.yandex.practicum.iteractionapi.dto.ShoppingCartDto;
import ru.yandex.practicum.iteractionapi.client.WarehouseClient;
import ru.yandex.practicum.iteractionapi.exception.WarehouseServerUnavailableException;
import ru.yandex.practicum.iteractionapi.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.iteractionapi.request.NewProductInWarehouseRequest;

import java.util.Map;
import java.util.UUID;

@Component
public class WarehouseClientFallback implements WarehouseClient {

    @Override
    public void addNewProductToWarehouse(NewProductInWarehouseRequest requestDto) {
        throw new WarehouseServerUnavailableException("Fallback response: сервис временно недоступен.");
    }

    @Override
    public void addProductToWarehouse(AddProductToWarehouseRequest requestDto) {
        throw new WarehouseServerUnavailableException("Fallback response: сервис временно недоступен.");
    }

    @Override
    public BookedProductsDto checkProductQuantityForCart(ShoppingCartDto shoppingCartDto) {
        throw new WarehouseServerUnavailableException("Fallback response: сервис временно недоступен.");
    }

    @Override
    public AddressDto fetchWarehouseAddress() {
        throw new WarehouseServerUnavailableException("Fallback response: сервис временно недоступен.");
    }

    @Override
    public BookedProductsDto bookingCartProducts(ShoppingCartDto shoppingCartDto) {
        throw new WarehouseServerUnavailableException("Fallback response: сервис временно недоступен.");
    }

    @Override
    public AddressDto getWarehouseAddress() {
        throw new WarehouseServerUnavailableException("Fallback response: сервис временно недоступен.");
    }

    @Override
    public void addReturnProductInWarehouse(Map<UUID, Long> products) {
        throw new WarehouseServerUnavailableException("Fallback response: сервис временно недоступен.");
    }

    @Override
    public BookedProductsDto createAssembly(AssemblyProductsForOrderRequest assembly) {
        throw new WarehouseServerUnavailableException("Fallback response: сервис временно недоступен.");
    }

    @Override
    public void shippedToDelivery(ShippedToDeliveryRequest deliveryRequest) {
        throw new WarehouseServerUnavailableException("Fallback response: сервис временно недоступен.");
    }
}