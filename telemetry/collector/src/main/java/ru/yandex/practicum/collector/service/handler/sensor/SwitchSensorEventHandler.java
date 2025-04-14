package ru.yandex.practicum.collector.service.handler.sensor;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.configuration.KafkaClient;
import ru.yandex.practicum.collector.configuration.KafkaTopicsConfig;
import ru.yandex.practicum.collector.model.event.sensor.SensorEvent;
import ru.yandex.practicum.collector.model.event.sensor.SensorEventType;
import ru.yandex.practicum.collector.model.event.sensor.SwitchSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
@RequiredArgsConstructor
public class SwitchSensorEventHandler implements SensorEventHandler {
    private final KafkaClient kafkaClient;
    private final KafkaTopicsConfig kafkaTopicsConfig;

    @Override
    public void handle(SensorEvent event) {
        SwitchSensorEvent switchSensorEvent = (SwitchSensorEvent) event;
        SwitchSensorAvro payload = SwitchSensorAvro.newBuilder()
                .setState(switchSensorEvent.getState())
                .build();
        SensorEventAvro avroEvent = SensorEventAvro.newBuilder()
                .setId(switchSensorEvent.getId())
                .setHubId(switchSensorEvent.getHubId())
                .setTimestamp(switchSensorEvent.getTimestamp())
                .setPayload(payload)
                .build();
        kafkaClient.getProducer().send(new ProducerRecord<>(kafkaTopicsConfig.getSensors(), avroEvent));
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}