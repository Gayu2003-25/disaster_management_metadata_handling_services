package com.kernelx.metadatahandling.service;

import com.kernelx.metadatahandling.entity.Sensor;
import com.kernelx.metadatahandling.repository.SensorRepository;
import org.springframework.stereotype.Service;

@Service
public class SensorService {

    private final SensorRepository repository;

    public SensorService(SensorRepository repository) {
        this.repository = repository;
    }

    public Sensor updateSensor(int id, Sensor sensorDetails) {

        Sensor sensor = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sensor not found with id " + id));

        sensor.setSensorType(sensorDetails.getSensorType());
        sensor.setSite(sensorDetails.getSite());
        sensor.setLatitude(sensorDetails.getLatitude());
        sensor.setLongitude(sensorDetails.getLongitude());
        sensor.setUnitOfMeasure(sensorDetails.getUnitOfMeasure());
        sensor.setThresholdHighWarning(sensorDetails.getThresholdHighWarning());
        sensor.setThresholdHighCritical(sensorDetails.getThresholdHighCritical());
        sensor.setThresholdLowWarning(sensorDetails.getThresholdLowWarning());
        sensor.setThresholdLowCritical(sensorDetails.getThresholdLowCritical());

        return repository.save(sensor);
    }

    public void deleteSensor(int id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException("Sensor not found with id " + id);
        }

        repository.deleteById(id);
    }
}