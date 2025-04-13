package ru.yandex.practicum.collector.model.event.hub.utility;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ScenarioCondition {
    @NotNull
    private String sensorId;

    @NotNull
    private ConditionType type;

    @NotNull
    private Operation operation;

    @NotNull
    private Integer value;
}