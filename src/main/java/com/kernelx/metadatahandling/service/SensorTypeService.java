package com.kernelx.metadatahandling.service;

import com.kernelx.metadatahandling.entity.SensorType;
import com.kernelx.metadatahandling.repository.SensorTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SensorTypeService {

    private final SensorTypeRepository repository;

    public SensorTypeService(SensorTypeRepository repository) {
        this.repository = repository;
    }

    public SensorType updateSensorType(int id, SensorType typeDetails) {

        SensorType type = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SensorType not found with id " + id));

        type.setType(typeDetails.getType());

        return repository.save(type);
    }

    public void deleteSensorType(int id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException("SensorType not found with id " + id);
        }

        repository.deleteById(id);
    }
}