package com.kernelx.metadatahandling.controller;

import com.kernelx.metadatahandling.entity.Sensor;
import com.kernelx.metadatahandling.service.SensorService;
import org.springframework.web.bind.annotation.*;

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
}