package ru.yandex.practicum.collector.service.handler.hub;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.configuration.KafkaClient;
import ru.yandex.practicum.collector.configuration.KafkaTopicsConfig;
import ru.yandex.practicum.collector.model.event.hub.HubEvent;
import ru.yandex.practicum.collector.model.event.hub.HubEventType;
import ru.yandex.practicum.collector.model.event.hub.ScenarioAddedEvent;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HubScenarioAddedEventHandler implements HubEventHandler {
    private final KafkaClient kafkaClient;
    private final KafkaTopicsConfig kafkaTopicsConfig;

    @Override
    public void handle(HubEvent event) {
        ScenarioAddedEvent scenarioAddedEvent = (ScenarioAddedEvent) event;
        List<DeviceActionAvro> avroActions = scenarioAddedEvent.getActions().stream()
                .map(action -> DeviceActionAvro.newBuilder()
                        .setType(ActionTypeAvro.valueOf(action.getType().name()))
                        .setSensorId(action.getSensorId())
                        .setValue(action.getValue())
                        .build())
                .toList();

        List<ScenarioConditionAvro> avroConditions = scenarioAddedEvent.getConditions().stream()
                .map(action -> ScenarioConditionAvro.newBuilder()
                        .setSensorId(action.getSensorId())
                        .setType(ConditionTypeAvro.valueOf(action.getType().name()))
                        .setOperation(ConditionOperationAvro.valueOf(action.getOperation().name()))
                        .setValue(action.getValue())
                        .build())
                .toList();

        ScenarioAddedEventAvro payload = ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedEvent.getName())
                .setActions(avroActions)
                .setConditions(avroConditions)
                .build();
        HubEventAvro avroEvent = HubEventAvro.newBuilder()
                .setHubId(scenarioAddedEvent.getHubId())
                .setPayload(payload)
                .setTimestamp(scenarioAddedEvent.getTimestamp())
                .build();
        kafkaClient.getProducer().send(new ProducerRecord<>(kafkaTopicsConfig.getHubs(), avroEvent));
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_ADDED;
    }
}