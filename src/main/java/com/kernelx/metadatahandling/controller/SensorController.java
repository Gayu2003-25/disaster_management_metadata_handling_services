package com.kernelx.metadatahandling.controller;

import com.kernelx.metadatahandling.entity.Sensor;
import com.kernelx.metadatahandling.repository.SensorRepository;
import com.kernelx.metadatahandling.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sensors")
public class SensorController {

    private final SensorService service;

    public SensorController(SensorService service) {
        this.service = service;
    }

    @PutMapping("/{id}")
    public Sensor updateSensor(@PathVariable int id, @RequestBody Sensor sensorDetails) {
        return service.updateSensor(id, sensorDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteSensor(@PathVariable int id) {
        service.deleteSensor(id);
    }

    @Autowired
    private SensorRepository sensorRepository;

    //View all
    @GetMapping
    public List<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }

    //Create new Sensor
    @PostMapping
    public Sensor createSensor(@RequestBody Sensor sensor) {
        return sensorRepository.save(sensor);
    }

    //View all by ID
    // View sensor by ID
    @GetMapping("/{id}")
    public Sensor getSensorById(@PathVariable Integer id) {
        return sensorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sensor not found with id " + id));
    }
}