package ru.yandex.practicum.analyzer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.analyzer.model.Action;
import ru.yandex.practicum.analyzer.model.Scenario;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;

@Service
@Slf4j
@RequiredArgsConstructor
public class HubRouterController {
    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    public void doAction(Scenario scenario, Action action, String hubId) {
        DeviceActionRequest request = DeviceActionRequest.newBuilder()
                .setHubId(hubId)
                .setScenarioName(scenario.getName())
                .setAction(DeviceActionProto.newBuilder()
                        .setSensorId(action.getSensorId())
                        .setType(ActionTypeProto.valueOf(action.getType().name()))
                        .setValue(action.getValue())
                        .build())
                .build();
        try {
            hubRouterClient.handleDeviceAction(request);
            log.info("Sent action to hub: {}", action);
        } catch (Exception e) {
            log.error("Failed to execute action for hubId {}: {}", hubId, e.getMessage());
        }
    }
}