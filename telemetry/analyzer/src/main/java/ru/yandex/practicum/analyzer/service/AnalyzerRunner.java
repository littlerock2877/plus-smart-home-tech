package ru.yandex.practicum.analyzer.service;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.configuration.kafka.KafkaClient;
import ru.yandex.practicum.configuration.kafka.KafkaTopicsConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnalyzerRunner {
    private static final int THREADS_COUNT = 2;
    private static final int SHUTDOWN_TIMEOUT_SECONDS = 60;

    private final KafkaClient kafkaClient;
    private final KafkaTopicsConfig kafkaTopicsConfig;
    private final AnalyzerService analyzerService;
    private final ScenarioService scenarioService;
    private final SensorService sensorService;
    private ExecutorService executorService = Executors.newFixedThreadPool(THREADS_COUNT);

    public void run() {
        executorService.execute(new HubEventProcessor(kafkaClient, kafkaTopicsConfig, sensorService, scenarioService));
        executorService.execute(new SnapshotProcessor(kafkaClient, kafkaTopicsConfig, analyzerService));
    }

    @PreDestroy
    public void dispose() {
        log.info("Start disposing executor service");
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                log.warn("Not all tasks finished during the timeout");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            log.error("Interrupted exception with message {} was thrown", e.getMessage());
            executorService.shutdownNow();
        }
    }
}