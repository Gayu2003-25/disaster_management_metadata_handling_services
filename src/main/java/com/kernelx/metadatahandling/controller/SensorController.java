package com.kernelx.metadatahandling.controller;

import com.kernelx.metadatahandling.entity.Sensor;
import com.kernelx.metadatahandling.service.SensorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "http://localhost:8090")
@RestController
@RequestMapping("/sensors") // changed from /sensors → /sensor
public class SensorController {

    private final SensorService service;

    public SensorController(SensorService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Sensor>> getAllSensors() {
        return ResponseEntity.ok(service.getAllSensors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sensor> getSensorById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getSensorById(id));
    }

    @PostMapping
    public ResponseEntity<Sensor> createSensor(@RequestBody Sensor sensor) {
        Sensor saved = service.createSensor(sensor);
        log.info("Created Sensor with ID: {}", saved.getSensorId());
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sensor> updateSensor(@PathVariable Integer id,
                                               @RequestBody Sensor details) {
        return ResponseEntity.ok(service.updateSensor(id, details));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSensor(@PathVariable Integer id) {
        service.deleteSensor(id);
        return ResponseEntity.noContent().build();
    }
}