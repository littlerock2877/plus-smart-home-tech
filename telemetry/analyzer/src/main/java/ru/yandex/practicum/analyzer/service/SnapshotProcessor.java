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
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotProcessor implements Runnable {
    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofMillis(1000);
    private static final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    private final KafkaClient kafkaClient;
    private final KafkaTopicsConfig kafkaTopicsConfig;
    private final AnalyzerService analyzerService;

    @Override
    public void run() {
        Consumer<String, SensorsSnapshotAvro> consumer = kafkaClient.getSnapshotConsumer();

        try {
            consumer.subscribe(List.of(kafkaTopicsConfig.getSnapshots()));

            while (true) {
                ConsumerRecords<String, SensorsSnapshotAvro> records = consumer.poll(CONSUME_ATTEMPT_TIMEOUT);
                int count = 0;
                for (ConsumerRecord<String, SensorsSnapshotAvro> record : records) {
                    analyzerService.handleSnapshot(record.value());
                    ConsumerUtil.manageOffsets(record, count, consumer, currentOffsets);
                    count++;
                }
                consumer.commitAsync();
            }
        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Error while handling snapshot", e);
        } finally {
            try {
                consumer.commitSync(currentOffsets);
            } finally {
                kafkaClient.stop();
            }
        }
    }
}