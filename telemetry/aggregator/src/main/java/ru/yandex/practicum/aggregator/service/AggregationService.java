package ru.yandex.practicum.aggregator.service;

import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public interface AggregationService {
    void handleSensorEvent(SensorEventAvro event);
}