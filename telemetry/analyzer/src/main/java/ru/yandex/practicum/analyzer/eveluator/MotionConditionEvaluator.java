package ru.yandex.practicum.analyzer.eveluator;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.model.ConditionOperation;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;

@Component
public class MotionConditionEvaluator extends ConditionEvaluator {
    @Override
    public boolean evaluate(Object sensorData, ConditionOperation operation, Integer value) {
        if (sensorData instanceof MotionSensorAvro motionSensor) {
            int motionValue = motionSensor.getMotion() ? 1 : 0;
            return evaluateCondition(motionValue, operation, value);
        }
        return false;
    }
}