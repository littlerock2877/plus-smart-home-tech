package ru.yandex.practicum.analyzer.eveluator;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.model.ConditionOperation;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Component
public class TemperatureConditionEvaluator extends ConditionEvaluator {
    @Override
    public boolean evaluate(Object sensorData, ConditionOperation operation, Integer value) {
        if (sensorData instanceof TemperatureSensorAvro tempSensor) {
            return evaluateCondition(tempSensor.getTemperatureC(), operation, value);
        }
        return false;
    }
}