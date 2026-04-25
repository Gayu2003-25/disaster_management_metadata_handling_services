package com.kernelx.metadatahandling.controller;

import com.kernelx.metadatahandling.entity.SensorType;
import com.kernelx.metadatahandling.repository.SensorTypeRepository;
import com.kernelx.metadatahandling.service.SensorTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/sensorTypes")
public class SensorTypeController {

    private final SensorTypeService service;

    @Autowired
    private SensorTypeRepository sensorTypeRepository;

    public SensorTypeController(SensorTypeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<SensorType>> getAllTypes() {
        List<SensorType> types = sensorTypeRepository.findAll();
        log.info("HTTP STATUS: 200 OK - Fetched {} sensor types", types.size());
        return ResponseEntity.ok(types);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SensorType> getSensorTypeById(@PathVariable Integer id) {
        SensorType type = sensorTypeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("HTTP STATUS: 404 NOT FOUND - SensorType ID {} not found", id);
                    return new RuntimeException("Sensor type not found with id " + id);
                });
        log.info("HTTP STATUS: 200 OK - Fetched SensorType: {}", type.getType());
        return ResponseEntity.ok(type);
    }

    @PostMapping
    public ResponseEntity<SensorType> createType(@RequestBody SensorType sensorType) {
        SensorType saved = sensorTypeRepository.save(sensorType);
        log.info("HTTP STATUS: 201 CREATED - Created SensorType: {}", saved.getType());
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SensorType> updateSensorType(@PathVariable int id, @RequestBody SensorType typeDetails) {
        SensorType updated = service.updateSensorType(id, typeDetails);
        log.info("HTTP STATUS: 200 OK - Updated SensorType ID: {}", id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSensorType(@PathVariable int id) {
        service.deleteSensorType(id);
        log.info("HTTP STATUS: 204 NO CONTENT - Deleted SensorType ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}