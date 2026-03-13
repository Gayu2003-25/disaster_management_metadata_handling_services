package com.kernelx.metadatahandling.controller;

import com.kernelx.metadatahandling.entity.SensorType;
import com.kernelx.metadatahandling.repository.SensorTypeRepository;
import com.kernelx.metadatahandling.service.SensorTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sensorTypes")
public class SensorTypeController {

    private final SensorTypeService service;

    public SensorTypeController(SensorTypeService service) {
        this.service = service;
    }

    @PutMapping("/{id}")
    public SensorType updateSensorType(@PathVariable int id, @RequestBody SensorType typeDetails) {
        return service.updateSensorType(id, typeDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteSensorType(@PathVariable int id) {
        service.deleteSensorType(id);
    }

    @Autowired
    private SensorTypeRepository sensorTypeRepository;

    //View all
    @GetMapping
    public List<SensorType> getAllTypes() {
        return sensorTypeRepository.findAll();
    }

    //Creat
    @PostMapping
    public SensorType createType(@RequestBody SensorType sensorType) {
        return sensorTypeRepository.save(sensorType);
    }

    // View sensor type by ID
    @GetMapping("/{id}")
    public SensorType getSensorTypeById(@PathVariable Integer id) {
        return sensorTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sensor type not found with id " + id));
    }
}