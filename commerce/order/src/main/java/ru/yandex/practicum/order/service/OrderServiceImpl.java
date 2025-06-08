package ru.yandex.practicum.order.service;

import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.iteractionapi.client.DeliveryClient;
import ru.yandex.practicum.iteractionapi.client.PaymentClient;
import ru.yandex.practicum.iteractionapi.client.WarehouseClient;
import ru.yandex.practicum.iteractionapi.dto.AddressDto;
import ru.yandex.practicum.iteractionapi.dto.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.iteractionapi.dto.BookedProductsDto;
import ru.yandex.practicum.iteractionapi.dto.delivery.DeliveryDto;
import ru.yandex.practicum.iteractionapi.dto.delivery.DeliveryState;
import ru.yandex.practicum.iteractionapi.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.iteractionapi.dto.order.OrderDto;
import ru.yandex.practicum.iteractionapi.dto.order.OrderState;
import ru.yandex.practicum.iteractionapi.dto.order.ProductReturnRequest;
import ru.yandex.practicum.order.mapper.AddressMapper;
import ru.yandex.practicum.order.mapper.OrderMapper;
import ru.yandex.practicum.order.model.Order;
import ru.yandex.practicum.order.model.OrderProduct;
import ru.yandex.practicum.order.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final WarehouseClient warehouseClient;
    private final AddressMapper addressMapper;
    private final OrderMapper orderMapper;
    private final PaymentClient paymentClient;
    private final DeliveryClient deliveryClient;

    @Override
    public OrderDto getOrders(String username) {
        return null;
    }

    @Transactional
    @Override
    public OrderDto saveOrder(CreateNewOrderRequest newOrderRequest) {
        Order order = new Order();

        Map<UUID, Long> products = newOrderRequest.getShoppingCart().getProducts();
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (Map.Entry<UUID, Long> entry : products.entrySet()) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setProductId(entry.getKey());
            orderProduct.setCount(entry.getValue());
            orderProducts.add(orderProduct);
        }

        order.setShoppingCartId(newOrderRequest.getShoppingCart().getShoppingCartId());
        order.setProducts(orderProducts);
        order.setState(OrderState.NEW);
        order.setDeliveryAddress(addressMapper.toOrderAddress(newOrderRequest.getDeliveryAddress()));

        Order savedOrder = orderRepository.save(order);
        DeliveryDto newDeliveryDto = new DeliveryDto();
        AddressDto warehouseAddress;
        try {
            warehouseAddress = warehouseClient.getWarehouseAddress();
        } catch (FeignException e) {
            log.warn("Failed to fetch warehouse address: {}", e.getMessage(), e);
            throw e;
        }

        newDeliveryDto.setToAddress(warehouseAddress);
        newDeliveryDto.setFromAddress(newOrderRequest.getDeliveryAddress());
        newDeliveryDto.setDeliveryState(DeliveryState.CREATED);
        newDeliveryDto.setOrderId(savedOrder.getOrderId());
        DeliveryDto savedDelivery;
        try {
            savedDelivery = deliveryClient.planDelivery(newDeliveryDto);
        } catch (FeignException e) {
            log.warn("Failed to plan delivery: {}", e.getMessage(), e);
            throw e;
        }

        savedOrder.setDeliveryId(savedDelivery.getDeliveryId());
        return orderMapper.toDto(savedOrder);
    }

    @Transactional
    @Override
    public OrderDto returnOrder(ProductReturnRequest orderDto) {
        Order order = getOrderFromRepository(orderDto.getOrderId());
        try {
            warehouseClient.addReturnProductInWarehouse(orderDto.getProducts());
        } catch (FeignException e) {
            log.warn("Failed to return product to warehouse: {}", e.getMessage(), e);
            throw e;
        }
        order.setState(OrderState.PRODUCT_RETURNED);
        Order saved = orderRepository.save(order);
        return orderMapper.toDto(saved);
    }

    @Transactional
    @Override
    public OrderDto paymentOrder(UUID orderId) {
        Order order = getOrderFromRepository(orderId);
        order.setState(OrderState.PAID);
        Order saved = orderRepository.save(order);
        return orderMapper.toDto(saved);
    }

    @Transactional
    @Override
    public OrderDto paymentFailedOrder(UUID orderId) {
        Order order = getOrderFromRepository(orderId);
        order.setState(OrderState.PAYMENT_FAILED);
        Order saved = orderRepository.save(order);
        return orderMapper.toDto(saved);
    }

    @Transactional
    @Override
    public OrderDto deliveryOrder(UUID orderId) {
        Order order = getOrderFromRepository(orderId);
        order.setState(OrderState.DELIVERED);
        Order saved = orderRepository.save(order);
        return orderMapper.toDto(saved);
    }

    @Transactional
    @Override
    public OrderDto deliveryFailedOrder(UUID orderId) {
        Order order = getOrderFromRepository(orderId);
        order.setState(OrderState.DELIVERY_FAILED);
        Order saved = orderRepository.save(order);
        return orderMapper.toDto(saved);
    }

    @Transactional
    @Override
    public OrderDto completedOrder(UUID orderId) {
        Order order = getOrderFromRepository(orderId);
        order.setState(OrderState.COMPLETED);
        Order saved = orderRepository.save(order);
        return orderMapper.toDto(saved);
    }

    @Transactional
    @Override
    public OrderDto calculateOrder(UUID orderId) {
        Order order = getOrderFromRepository(orderId);
        OrderDto dto = orderMapper.toDto(order);
        Double productCost;
        try {
            productCost = paymentClient.calculateProductCost(dto);

        } catch (FeignException e) {
            log.warn("Failed to calculate product cost: {}", e.getMessage(), e);
            throw e;
        }
        order.setProductPrice(productCost);
        dto.setProductPrice(productCost);

        Double totalCost;
        try {
            totalCost = paymentClient.calculateTotalCost(dto);
        } catch (FeignException e) {
            log.warn("Failed to calculate total cost: {}", e.getMessage(), e);
            throw e;
        }
        order.setTotalPrice(totalCost);
        dto.setTotalPrice(totalCost);
        return dto;
    }

    @Transactional
    @Override
    public OrderDto calculateOrderDelivery(UUID orderId) {
        Order order = getOrderFromRepository(orderId);
        OrderDto dto = orderMapper.toDto(order);
        Double deliveryCost = deliveryClient.calculateFullDeliveryCost(dto);
        order.setDeliveryPrice(deliveryCost);
        Order saved = orderRepository.save(order);
        return orderMapper.toDto(saved);
    }

    @Transactional
    @Override
    public OrderDto assembleOrder(UUID orderId) {
        Order savedOrder = getOrderFromRepository(orderId);
        AssemblyProductsForOrderRequest assembly = new AssemblyProductsForOrderRequest();
        assembly.setOrderId(orderId);

        BookedProductsDto clientAssembly;
        try {
            clientAssembly = warehouseClient.createAssembly(assembly);
        } catch (FeignException e) {
            log.warn("Failed to create assembly: {}", e.getMessage(), e);
            throw e;
        }
        savedOrder.setDeliveryWeight(clientAssembly.getDeliveryWeight());
        savedOrder.setDeliveryVolume(clientAssembly.getDeliveryVolume());
        savedOrder.setState(OrderState.ASSEMBLED);
        Order saved = orderRepository.save(savedOrder);
        return orderMapper.toDto(saved);
    }

    @Transactional
    @Override
    public OrderDto assembleFailedOrder(UUID orderId) {
        Order order = getOrderFromRepository(orderId);
        order.setState(OrderState.ASSEMBLY_FAILED);
        Order saved = orderRepository.save(order);
        return orderMapper.toDto(saved);
    }

    private Order getOrderFromRepository(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order with id" + orderId + " not found "));
    }
}