package ru.yandex.practicum.configuration.kafka;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public interface KafkaClient {
    Producer<String, SpecificRecordBase> getProducer();

    Consumer<String, SensorEventAvro> getSensorEventConsumer();

    Consumer<String, SensorsSnapshotAvro> getSnapshotConsumer();

    Consumer<String, HubEventAvro> getHubEventConsumer();

    void stop();
}