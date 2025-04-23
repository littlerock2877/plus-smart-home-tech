package ru.yandex.practicum.collector.service.event;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.collector.service.handler.sensor.SensorEventHandler;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SensorEventServiceImpl implements SensorEventService {
    private final Map<SensorEventProto.PayloadCase, SensorEventHandler> sensorEventHandlers;

    public SensorEventServiceImpl(List<SensorEventHandler> sensorEventHandlers) {
        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getMessageType, Function.identity()));
    }

    @Override
    public void handleEvent(SensorEventProto request) {
        if (sensorEventHandlers.containsKey(request.getPayloadCase())) {
            sensorEventHandlers.get(request.getPayloadCase()).handle(request);
        } else {
            throw new IllegalArgumentException(String.format("Handler for event with type %s not found", request.getPayloadCase()));
        }
    }
}