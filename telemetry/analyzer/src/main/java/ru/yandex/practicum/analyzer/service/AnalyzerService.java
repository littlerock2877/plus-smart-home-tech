package ru.yandex.practicum.analyzer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.analyzer.grpc.HubRouterClient;
import ru.yandex.practicum.analyzer.model.Action;
import ru.yandex.practicum.analyzer.model.Condition;
import ru.yandex.practicum.analyzer.model.ConditionOperation;
import ru.yandex.practicum.analyzer.model.Scenario;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

import java.util.List;

import static ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro.EQUALS;
import static ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro.GREATER_THAN;
import static ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro.LOWER_THAN;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyzerService {

    private final ScenarioService scenarioService;
    private final HubRouterClient hubRouterClient;

    public void processSnapshot(SensorsSnapshotAvro snapshot) {
        String hubId = snapshot.getHubId();
        log.info("Processing snapshot for hubId: {}", hubId);

        List<Scenario> scenarios = scenarioService.getScenariosByHubId(hubId);

        for (Scenario scenario : scenarios) {
            if (isScenarioTriggered(scenario, snapshot)) {
                executeActions(scenario.getActions(), hubId);
            }
        }
    }

    private boolean isScenarioTriggered(Scenario scenario, SensorsSnapshotAvro snapshot) {
        for (Condition condition : scenario.getConditions()) {
            if (!checkCondition(condition, snapshot)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkCondition(Condition condition, SensorsSnapshotAvro snapshot) {
        SensorStateAvro sensorState = snapshot.getSensorsState().get(condition.getSensorId());

        if (sensorState == null) {
            log.warn("Sensor data for sensorId {} is missing in the snapshot", condition.getSensorId());
            return false;
        }

        try {
            // Проверка типа данных и приведение
            switch (condition.getType()) {
                case TEMPERATURE:
                    if (sensorState.getData() instanceof TemperatureSensorAvro tempSensor) {
                        return evaluateCondition(tempSensor.getTemperatureC(), condition.getOperation(), condition.getValue());
                    }
                    break;
                case HUMIDITY:
                    if (sensorState.getData() instanceof ClimateSensorAvro climateSensor) {
                        return evaluateCondition(climateSensor.getHumidity(), condition.getOperation(), condition.getValue());
                    }
                    break;
                case CO2LEVEL:
                    if (sensorState.getData() instanceof ClimateSensorAvro climateSensor) {
                        return evaluateCondition(climateSensor.getCo2Level(), condition.getOperation(), condition.getValue());
                    }
                    break;
                case LUMINOSITY:
                    if (sensorState.getData() instanceof LightSensorAvro lightSensor) {
                        return evaluateCondition(lightSensor.getLuminosity(), condition.getOperation(), condition.getValue());
                    }
                    break;
                case MOTION:
                    if (sensorState.getData() instanceof MotionSensorAvro motionSensor) {
                        int motionValue = motionSensor.getMotion() ? 1 : 0;
                        return evaluateCondition(motionValue, condition.getOperation(), condition.getValue());
                    }
                    break;
                case SWITCH:
                    if (sensorState.getData() instanceof SwitchSensorAvro switchSensor) {
                        int switchState = switchSensor.getState() ? 1 : 0;
                        return evaluateCondition(switchState, condition.getOperation(), condition.getValue());
                    }
                    break;
                default:
                    log.warn("Unsupported condition type: {}", condition.getType());
                    return false;
            }
        } catch (Exception e) {
            log.error("Error checking condition {}: {}", condition, e.getMessage());
        }
        return false;
    }

    private boolean evaluateCondition(int sensorValue, ConditionOperation operation, int targetValue) {
        return switch (operation) {
            case ConditionOperation.EQUAL -> sensorValue == targetValue;
            case ConditionOperation.MORE -> sensorValue > targetValue;
            case ConditionOperation.LESS -> sensorValue < targetValue;
        };
    }

    private void executeActions(List<Action> actions, String hubId) {
        for (Action action : actions) {
            hubRouterClient.executeAction(action, hubId);
            log.info("Executing action: {} for hubId: {}", action, hubId);
        }
    }
}