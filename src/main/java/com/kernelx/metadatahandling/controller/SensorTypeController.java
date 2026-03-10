package com.kernelx.metadatahandling.controller;

import com.kernelx.metadatahandling.entity.SensorType;
import com.kernelx.metadatahandling.service.SensorTypeService;
import org.springframework.web.bind.annotation.*;

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
}