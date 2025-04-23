package ru.yandex.practicum.collector.service.event;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

public interface SensorEventService {
    void handleEvent(SensorEventProto event);
}