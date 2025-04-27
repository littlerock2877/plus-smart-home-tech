package ru.yandex.practicum.kafka.deserializer;

import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public class SnapshotDeserializer extends GeneralAvroDeserializer<SensorsSnapshotAvro> {
    public SnapshotDeserializer() {
        super(SensorsSnapshotAvro.getClassSchema());
    }
}