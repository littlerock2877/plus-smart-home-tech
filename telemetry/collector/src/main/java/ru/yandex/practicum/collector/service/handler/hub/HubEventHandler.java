package ru.yandex.practicum.collector.service.handler.hub;

import ru.yandex.practicum.collector.model.event.hub.HubEvent;
import ru.yandex.practicum.collector.model.event.hub.HubEventType;

public interface HubEventHandler {
    HubEventType getMessageType();

    void handle(HubEvent event);
}