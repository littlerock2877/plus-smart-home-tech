package ru.yandex.practicum.collector.service.event;

import ru.yandex.practicum.collector.model.event.hub.HubEvent;

public interface HubEventService {
    void handleEvent(HubEvent event);
}