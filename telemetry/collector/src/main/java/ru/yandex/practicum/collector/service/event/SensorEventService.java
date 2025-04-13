package ru.yandex.practicum.collector.service.event;

import ru.yandex.practicum.collector.model.event.sensor.SensorEvent;

public interface SensorEventService {
    void handleEvent(SensorEvent event);
}