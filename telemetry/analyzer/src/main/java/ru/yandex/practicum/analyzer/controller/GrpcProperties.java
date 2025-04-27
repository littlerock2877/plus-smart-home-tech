package ru.yandex.practicum.analyzer.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.grpc.client.hub-router")
public class GrpcProperties {
    private String address;
    private boolean enableKeepAlive;
    private boolean keepAliveWithoutCalls;
    private String negotiationType;
}