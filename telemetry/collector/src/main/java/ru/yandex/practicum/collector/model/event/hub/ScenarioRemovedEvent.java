package ru.yandex.practicum.collector.model.event.hub;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
public class ScenarioRemovedEvent extends HubEvent {
    @NotBlank
    @Length(min = 3)
    private String name;


    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}