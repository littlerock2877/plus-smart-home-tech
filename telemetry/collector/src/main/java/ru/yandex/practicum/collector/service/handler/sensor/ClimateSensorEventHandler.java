package ru.yandex.practicum.collector.service.handler.sensor;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.configuration.KafkaClient;
import ru.yandex.practicum.collector.configuration.KafkaTopicsConfig;
import ru.yandex.practicum.collector.model.event.sensor.ClimateSensorEvent;
import ru.yandex.practicum.collector.model.event.sensor.SensorEvent;
import ru.yandex.practicum.collector.model.event.sensor.SensorEventType;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Component
@RequiredArgsConstructor
public class ClimateSensorEventHandler implements SensorEventHandler {
    private final KafkaClient kafkaClient;
    private final KafkaTopicsConfig kafkaTopicsConfig;

    @Override
    public void handle(SensorEvent event) {
        ClimateSensorEvent climateSensorEvent = (ClimateSensorEvent) event;
        ClimateSensorAvro payload = ClimateSensorAvro.newBuilder()
                .setTemperatureC(climateSensorEvent.getTemperatureC())
                .setCo2Level(climateSensorEvent.getCo2Level())
                .setHumidity(climateSensorEvent.getHumidity())
                .build();
        SensorEventAvro avroEvent = SensorEventAvro.newBuilder()
                .setId(climateSensorEvent.getId())
                .setHubId(climateSensorEvent.getHubId())
                .setTimestamp(climateSensorEvent.getTimestamp())
                .setPayload(payload)
                .build();
        kafkaClient.getProducer().send(new ProducerRecord<>(kafkaTopicsConfig.getSensors(), avroEvent));
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}