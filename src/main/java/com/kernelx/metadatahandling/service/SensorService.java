package com.kernelx.metadatahandling.service;

import com.kernelx.metadatahandling.entity.Sensor;
import com.kernelx.metadatahandling.repository.SensorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SensorService {

    private final SensorRepository repository;

    public SensorService(SensorRepository repository) {
        this.repository = repository;
    }

    public List<Sensor> getAllSensors() {
        return repository.findAll();
    }

    public Sensor getSensorById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sensor not found with ID: " + id));
    }

    public Sensor createSensor(Sensor sensor) {
        // If you're NOT auto-generating IDs, enforce presence
        if (sensor.getSensorId() == null) {
            throw new RuntimeException("Error: Sensor ID must be provided.");
        }

        return repository.save(sensor);
    }

    public Sensor updateSensor(Integer id, Sensor details) {
        Sensor existing = getSensorById(id);

        existing.setSensorType(details.getSensorType());
        existing.setSite(details.getSite());
        existing.setLatitude(details.getLatitude());
        existing.setLongitude(details.getLongitude());
        existing.setUnitOfMeasure(details.getUnitOfMeasure());
        existing.setThresholdHighWarning(details.getThresholdHighWarning());
        existing.setThresholdHighCritical(details.getThresholdHighCritical());
        existing.setThresholdLowWarning(details.getThresholdLowWarning());
        existing.setThresholdLowCritical(details.getThresholdLowCritical());

        return repository.save(existing);
    }

    public void deleteSensor(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Sensor not found with ID: " + id);
        }
        repository.deleteById(id);
    }
}