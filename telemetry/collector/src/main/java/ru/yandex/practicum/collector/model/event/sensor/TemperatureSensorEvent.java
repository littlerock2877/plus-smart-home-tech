package ru.yandex.practicum.collector.model.event.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TemperatureSensorEvent extends SensorEvent {
    @NotNull
    private Integer temperatureC;

    @NotNull
    private Integer temperatureF;

    @Override
    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}