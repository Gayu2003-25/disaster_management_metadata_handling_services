package com.kernelx.metadatahandling.service;

import com.kernelx.metadatahandling.entity.Sensor;
import com.kernelx.metadatahandling.entity.SensorType;
import com.kernelx.metadatahandling.entity.Site;
import com.kernelx.metadatahandling.repository.SensorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SensorServiceTest {

    @Mock
    private SensorRepository repository;

    @InjectMocks
    private SensorService sensorService;

    private Sensor sampleSensor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Site site = new Site();
        site.setSiteId(1);
        site.setSiteName("Station A");

        SensorType type = new SensorType();
        type.setSensorTypeId(1);
        type.setType("Water Level");

        sampleSensor = new Sensor();
        sampleSensor.setSensorId("sensor-001");
        sampleSensor.setSite(site);
        sampleSensor.setSensorType(type);
        sampleSensor.setLatitude(6.9271);
        sampleSensor.setLongitude(79.8612);
        sampleSensor.setUnitOfMeasure("m");
        sampleSensor.setThresholdHighWarning(5.0);
        sampleSensor.setThresholdHighCritical(8.0);
        sampleSensor.setThresholdLowWarning(1.0);
        sampleSensor.setThresholdLowCritical(0.5);
    }

    @Test
    void testGetAllSensors() {
        when(repository.findAll()).thenReturn(Arrays.asList(sampleSensor));

        List<Sensor> result = sensorService.getAllSensors();

        assertEquals(1, result.size());
        assertEquals("sensor-001", result.get(0).getSensorId());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetSensorByIdSuccess() {
        when(repository.findById("sensor-001")).thenReturn(Optional.of(sampleSensor));

        Sensor result = sensorService.getSensorById("sensor-001");

        assertNotNull(result);
        assertEquals("sensor-001", result.getSensorId());
        verify(repository, times(1)).findById("sensor-001");
    }

    @Test
    void testGetSensorByIdNotFound() {
        when(repository.findById("sensor-unknown")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> sensorService.getSensorById("sensor-unknown"));
        assertTrue(exception.getMessage().contains("Sensor not found with ID: sensor-unknown"));
        verify(repository, times(1)).findById("sensor-unknown");
    }

    @Test
    void testCreateSensorSuccess() {
        when(repository.save(any(Sensor.class))).thenReturn(sampleSensor);

        Sensor created = sensorService.createSensor(sampleSensor);

        assertNotNull(created);
        assertEquals("sensor-001", created.getSensorId());
        verify(repository, times(1)).save(sampleSensor);
    }

    @Test
    void testCreateSensorMissingId() {
        Sensor invalidSensor = new Sensor();
        invalidSensor.setSensorId(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> sensorService.createSensor(invalidSensor));
        assertTrue(exception.getMessage().contains("Error: Sensor ID must be provided."));
        verify(repository, never()).save(any());
    }

    @Test
    void testUpdateSensorSuccess() {
        when(repository.findById("sensor-001")).thenReturn(Optional.of(sampleSensor));
        when(repository.save(any(Sensor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Sensor updatedDetails = new Sensor();
        updatedDetails.setThresholdHighWarning(6.0);
        updatedDetails.setThresholdHighCritical(9.0);

        Sensor result = sensorService.updateSensor("sensor-001", updatedDetails);

        assertNotNull(result);
        assertEquals(6.0, result.getThresholdHighWarning());
        assertEquals(9.0, result.getThresholdHighCritical());
        verify(repository, times(1)).findById("sensor-001");
        verify(repository, times(1)).save(sampleSensor);
    }

    @Test
    void testDeleteSensorSuccess() {
        when(repository.existsById("sensor-001")).thenReturn(true);
        doNothing().when(repository).deleteById("sensor-001");

        assertDoesNotThrow(() -> sensorService.deleteSensor("sensor-001"));

        verify(repository, times(1)).existsById("sensor-001");
        verify(repository, times(1)).deleteById("sensor-001");
    }

    @Test
    void testDeleteSensorNotFound() {
        when(repository.existsById("sensor-999")).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> sensorService.deleteSensor("sensor-999"));
        assertTrue(exception.getMessage().contains("Sensor not found with ID: sensor-999"));
        verify(repository, times(1)).existsById("sensor-999");
        verify(repository, never()).deleteById(anyString());
    }
}
