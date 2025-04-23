package ru.yandex.practicum.collector.service.handler.hub;

import com.google.protobuf.util.Timestamps;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.configuration.KafkaClient;
import ru.yandex.practicum.collector.configuration.KafkaTopicsConfig;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HubScenarioAddedEventHandler implements HubEventHandler {
    private final KafkaClient kafkaClient;
    private final KafkaTopicsConfig kafkaTopicsConfig;

    @Override
    public void handle(HubEventProto event) {
        ScenarioAddedEventProto scenarioAddedEvent = event.getScenarioAdded();
        List<DeviceActionAvro> avroActions = scenarioAddedEvent.getActionsList().stream()
                .map(action -> DeviceActionAvro.newBuilder()
                        .setType(ActionTypeAvro.valueOf(action.getType().name()))
                        .setSensorId(action.getSensorId())
                        .setValue(action.getValue())
                        .build())
                .toList();

        List<ScenarioConditionAvro> avroConditions = scenarioAddedEvent.getConditionsList().stream()
                .map(action -> ScenarioConditionAvro.newBuilder()
                        .setSensorId(action.getSensorId())
                        .setType(ConditionTypeAvro.valueOf(action.getType().name()))
                        .setOperation(ConditionOperationAvro.valueOf(action.getOperation().name()))
                        .setValue(action.getIntValue())
                        .build())
                .toList();

        ScenarioAddedEventAvro payload = ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedEvent.getName())
                .setActions(avroActions)
                .setConditions(avroConditions)
                .build();
        HubEventAvro avroEvent = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setPayload(payload)
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .build();
        kafkaClient.getProducer().send(new ProducerRecord<>(
                kafkaTopicsConfig.getHubs(),
                null,
                Timestamps.toMillis(event.getTimestamp()),
                event.getHubId(),
                avroEvent));
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }
}