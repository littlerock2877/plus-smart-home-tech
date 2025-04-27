package ru.yandex.practicum.aggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.aggregator.starter.AggregationStarter;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableConfigurationProperties
@ComponentScan(basePackages = {
        "ru.yandex.practicum.aggregator",
        "ru.yandex.practicum.configuration.kafka"
})
public class Aggregator {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Aggregator.class, args);
        AggregationStarter aggregator = context.getBean(AggregationStarter.class);
        aggregator.start();
    }
}