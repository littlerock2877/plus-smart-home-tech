package ru.yandex.practicum.collector.service.handler.hub;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.configuration.KafkaClient;
import ru.yandex.practicum.collector.configuration.KafkaTopicsConfig;
import ru.yandex.practicum.collector.model.event.hub.DeviceRemovedEvent;
import ru.yandex.practicum.collector.model.event.hub.HubEvent;
import ru.yandex.practicum.collector.model.event.hub.HubEventType;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Component
@RequiredArgsConstructor
public class HubDeviceRemovedEventHandler implements HubEventHandler {
    private final KafkaClient kafkaClient;
    private final KafkaTopicsConfig kafkaTopicsConfig;

    @Override
    public void handle(HubEvent event) {
        DeviceRemovedEvent deviceRemovedEvent = (DeviceRemovedEvent) event;
        DeviceRemovedEventAvro payload = DeviceRemovedEventAvro.newBuilder()
                .setId(deviceRemovedEvent.getHubId())
                .build();
        HubEventAvro avroEvent = HubEventAvro.newBuilder()
                .setHubId(deviceRemovedEvent.getHubId())
                .setPayload(payload)
                .setTimestamp(deviceRemovedEvent.getTimestamp())
                .build();
        kafkaClient.getProducer().send(new ProducerRecord<>(kafkaTopicsConfig.getHubs(), avroEvent));
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_REMOVED;
    }
}