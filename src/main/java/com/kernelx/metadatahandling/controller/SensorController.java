package com.kernelx.metadatahandling.controller;

import com.kernelx.metadatahandling.entity.Sensor;
import com.kernelx.metadatahandling.repository.SensorRepository;
import com.kernelx.metadatahandling.service.SensorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/sensors")
public class SensorController {

    private final SensorService service;

    @Autowired
    private SensorRepository sensorRepository;

    public SensorController(SensorService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Sensor>> getAllSensors() {
        List<Sensor> sensors = sensorRepository.findAll();
        log.info("HTTP STATUS: 200 OK - Fetched {} sensors", sensors.size());
        return ResponseEntity.ok(sensors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sensor> getSensorById(@PathVariable int id) {
        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("HTTP STATUS: 404 NOT FOUND - Sensor ID {} not found", id);
                    return new RuntimeException("Sensor not found with id " + id);
                });
        log.info("HTTP STATUS: 200 OK - Fetched Sensor ID: {}", id);
        return ResponseEntity.ok(sensor);
    }

    @PostMapping
    public ResponseEntity<Sensor> createSensor(@RequestBody Sensor sensor) {
        Sensor savedSensor = sensorRepository.save(sensor);
        log.info("HTTP STATUS: 201 CREATED - Created Sensor ID: {}", savedSensor.getId());
        return new ResponseEntity<>(savedSensor, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sensor> updateSensor(@PathVariable int id, @RequestBody Sensor sensorDetails) {
        Sensor updated = service.updateSensor(id, sensorDetails);
        log.info("HTTP STATUS: 200 OK - Updated Sensor ID: {}", id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSensor(@PathVariable int id) {
        service.deleteSensor(id);
        log.info("HTTP STATUS: 204 NO CONTENT - Deleted Sensor ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}