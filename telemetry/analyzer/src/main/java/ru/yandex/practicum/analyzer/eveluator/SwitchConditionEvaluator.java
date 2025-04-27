package ru.yandex.practicum.analyzer.eveluator;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.model.ConditionOperation;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
public class SwitchConditionEvaluator extends ConditionEvaluator {
    @Override
    public boolean evaluate(Object sensorData, ConditionOperation operation, Integer value) {
        if (sensorData instanceof SwitchSensorAvro switchSensor) {
            int switchState = switchSensor.getState() ? 1 : 0;
            return evaluateCondition(switchState, operation, value);
        }
        return false;
    }
}