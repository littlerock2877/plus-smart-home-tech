package ru.yandex.practicum.collector.service.event;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

public interface HubEventService {
    void handleEvent(HubEventProto event);
}