package ru.yandex.practicum.collector.model.event.hub;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.collector.model.event.hub.utility.DeviceType;

@Getter
@Setter
@ToString
public class DeviceAddedEvent extends HubEvent {
    @NotNull
    private String id;

    @NotNull
    private DeviceType deviceType;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }
}