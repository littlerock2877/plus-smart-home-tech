package ru.yandex.practicum.analyzer.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.analyzer.model.Sensor;
import ru.yandex.practicum.analyzer.repository.SensorRepository;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorService {
    private final SensorRepository sensorRepository;

    @Transactional
    public Sensor addSensor(String deviceId, String hubId) {
        if (isSensorExist(deviceId)) {
            throw new IllegalArgumentException("Sensor with id " + deviceId + " already exists");
        }
        return sensorRepository.save(new Sensor(deviceId, hubId));
    }

    @Transactional
    public void removeSensor(String deviceId, String hubId) {
        Sensor savedSensor = findSensorById(deviceId);
        sensorRepository.delete(savedSensor);
    }


    private Sensor findSensorById(String id) {
        return sensorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Not found sensor with id " + id));
    }

    private boolean isSensorExist(String deviceId) {
        return sensorRepository.existsById(deviceId);
    }
}