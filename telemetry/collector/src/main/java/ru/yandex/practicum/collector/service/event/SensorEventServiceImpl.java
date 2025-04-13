package ru.yandex.practicum.collector.service.event;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.collector.model.event.sensor.SensorEvent;
import ru.yandex.practicum.collector.model.event.sensor.SensorEventType;
import ru.yandex.practicum.collector.service.handler.SensorEventHandler;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SensorEventServiceImpl implements SensorEventService {
    private final Map<SensorEventType, SensorEventHandler> sensorEventHandlers;

    public SensorEventServiceImpl(List<SensorEventHandler> sensorEventHandlers) {
        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getMessageType, Function.identity()));
    }

    @Override
    public void handleEvent(SensorEvent request) {
        if (sensorEventHandlers.containsKey(request.getType())) {
            sensorEventHandlers.get(request.getType()).handle(request);
        } else {
            throw new IllegalArgumentException(String.format("Handler for event with type %s not found", request.getType()));
        }
    }
}