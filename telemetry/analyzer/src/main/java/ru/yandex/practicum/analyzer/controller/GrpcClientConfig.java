package ru.yandex.practicum.analyzer.controller;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;

@Configuration
public class GrpcClientConfig {

    private final GrpcProperties grpcProperties;

    public GrpcClientConfig(GrpcProperties grpcProperties) {
        this.grpcProperties = grpcProperties;
    }

    @Bean
    public HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient() {
        ManagedChannelBuilder<?> channelBuilder = ManagedChannelBuilder
                .forTarget(grpcProperties.getAddress())
                .keepAliveWithoutCalls(grpcProperties.isKeepAliveWithoutCalls());

        if ("plaintext".equals(grpcProperties.getNegotiationType())) {
            channelBuilder.usePlaintext();
        }

        ManagedChannel channel = channelBuilder.build();
        return HubRouterControllerGrpc.newBlockingStub(channel);
    }
}