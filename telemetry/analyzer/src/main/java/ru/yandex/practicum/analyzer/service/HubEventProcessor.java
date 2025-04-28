package ru.yandex.practicum.analyzer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.util.ConsumerUtil;
import ru.yandex.practicum.configuration.kafka.KafkaClient;
import ru.yandex.practicum.configuration.kafka.KafkaTopicsConfig;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubEventProcessor implements Runnable {
    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofMillis(1000);
    private static final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    private final KafkaClient kafkaClient;
    private final KafkaTopicsConfig kafkaTopicsConfig;
    private final SensorService sensorService;
    private final ScenarioService scenarioService;

    @Override
    public void run() {
        Consumer<String, HubEventAvro> consumer = kafkaClient.getHubEventConsumer();

        try {
            consumer.subscribe(List.of(kafkaTopicsConfig.getHubs()));

            while (true) {
                ConsumerRecords<String, HubEventAvro> records = consumer.poll(CONSUME_ATTEMPT_TIMEOUT);
                int count = 0;
                for (ConsumerRecord<String, HubEventAvro> record : records) {
                    sendToService(record);
                    ConsumerUtil.manageOffsets(record, count, consumer, currentOffsets);
                    count++;
                }
                consumer.commitAsync();
            }
        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Error while handling hub event", e);
        } finally {
            try {
                consumer.commitSync(currentOffsets);
            } finally {
                kafkaClient.stop();
            }
        }
    }

    private void sendToService(ConsumerRecord<String, HubEventAvro> record) {
        HubEventAvro event = record.value();
        switch (event.getPayload()) {
            case DeviceAddedEventAvro deviceAddedEvent ->
                    sensorService.addSensor(deviceAddedEvent.getId(), event.getHubId());
            case DeviceRemovedEventAvro deviceRemovedEvent ->
                    sensorService.removeSensor(deviceRemovedEvent.getId(), event.getHubId());
            case ScenarioAddedEventAvro scenarioAddedEvent ->
                    scenarioService.addScenario(scenarioAddedEvent, event.getHubId());
            case ScenarioRemovedEventAvro scenarioRemovedEvent ->
                    scenarioService.deleteScenario(scenarioRemovedEvent.getName());
            case null, default -> log.warn("Unknown event type: {}", event.getPayload().getClass().getName());
        }
    }
}