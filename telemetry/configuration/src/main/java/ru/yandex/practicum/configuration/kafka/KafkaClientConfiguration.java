package ru.yandex.practicum.configuration.kafka;

import jakarta.annotation.PreDestroy;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

@Configuration
public class KafkaClientConfiguration {
    @Value("${kafka.consumer.group-id}")
    private String groupId;

    @Value("${kafka.consumer.client-id}")
    private String clientId;

    private AtomicBoolean shuttingDown = new AtomicBoolean(false);

    @Bean
    KafkaClient getClient() {
        return new KafkaClient() {
            private Consumer<String, SpecificRecordBase> consumer;
            private Producer<String, SpecificRecordBase> producer;

            @Override
            public Consumer<String, SpecificRecordBase> getConsumer() {
                if (consumer == null) {
                    initConsumer();
                }
                return consumer;
            }

            @Override
            public Producer<String, SpecificRecordBase> getProducer() {
                if (producer == null) {
                    initProducer();
                }
                return producer;
            }

            private void initProducer() {
                Properties config = new Properties();
                config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
                config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
                config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "ru.yandex.practicum.kafka.serializer.GeneralAvroSerializer");

                producer = new KafkaProducer<>(config);
            }

            private void initConsumer() {
                Properties config = new Properties();
                config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
                config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
                config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "ru.yandex.practicum.kafka.deserializer.SensorEventDeserializer");
                config.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
                config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
                config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

                consumer = new KafkaConsumer<>(config);
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    shuttingDown.set(true);
                    consumer.wakeup();
                }));
            }

            @PreDestroy
            @Override
            public void stop() {
                if (consumer != null) {
                    if (!shuttingDown.get()) {
                        consumer.wakeup();
                    }
                    consumer.close();
                }

                if (producer != null) {
                    producer.flush();
                    producer.close();
                }
            }
        };
    }
}