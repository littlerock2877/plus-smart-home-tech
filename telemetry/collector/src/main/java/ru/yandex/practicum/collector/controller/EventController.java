package ru.yandex.practicum.collector.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.collector.model.event.hub.HubEvent;
import ru.yandex.practicum.collector.model.event.sensor.SensorEvent;
import ru.yandex.practicum.collector.service.event.HubEventService;
import ru.yandex.practicum.collector.service.event.SensorEventService;

@Validated
@RestController
@Slf4j
@RequestMapping(path = "/events", consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class EventController {
    private final HubEventService hubEventService;
    private final SensorEventService sensorEventService;

    @PostMapping("/sensors")
    public void collectSensorEvent(@Valid @RequestBody SensorEvent request) {
        log.info("Handling sensor event {} - Started", request);
        sensorEventService.handleEvent(request);
        log.info("Handling sensor event {} - Finished", request);
    }

    @PostMapping("/hubs")
    public void collectHubEvent(@Valid @RequestBody HubEvent request) {
        log.info("Handling hub event {} - Started", request);
        hubEventService.handleEvent(request);
        log.info("Handling hub event {} - Finished", request);
    }
}