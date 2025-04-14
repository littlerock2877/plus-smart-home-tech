package ru.yandex.practicum.collector.configuration;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;

public interface KafkaClient {
    Producer<String, SpecificRecordBase> getProducer();

    Consumer<String, SpecificRecordBase> getConsumer();

    void stop();
}