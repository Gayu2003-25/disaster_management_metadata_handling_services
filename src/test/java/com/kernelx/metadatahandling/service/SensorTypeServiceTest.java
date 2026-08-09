package com.kernelx.metadatahandling.service;

import com.kernelx.metadatahandling.entity.SensorType;
import com.kernelx.metadatahandling.repository.SensorTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SensorTypeServiceTest {

    @Mock
    private SensorTypeRepository repository;

    @InjectMocks
    private SensorTypeService sensorTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateSensorTypeSuccess() {
        SensorType existingType = new SensorType();
        existingType.setSensorTypeId(1);
        existingType.setType("Rainfall");

        SensorType updatedDetails = new SensorType();
        updatedDetails.setType("Water Level");

        when(repository.findById(1)).thenReturn(Optional.of(existingType));
        when(repository.save(any(SensorType.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SensorType result = sensorTypeService.updateSensorType(1, updatedDetails);

        assertNotNull(result);
        assertEquals("Water Level", result.getType());
        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(existingType);
    }

    @Test
    void testUpdateSensorTypeNotFound() {
        SensorType updatedDetails = new SensorType();
        updatedDetails.setType("Temperature");

        when(repository.findById(999)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> sensorTypeService.updateSensorType(999, updatedDetails));
        assertTrue(exception.getMessage().contains("SensorType not found with id 999"));
        verify(repository, times(1)).findById(999);
        verify(repository, never()).save(any());
    }

    @Test
    void testDeleteSensorTypeSuccess() {
        when(repository.existsById(1)).thenReturn(true);
        doNothing().when(repository).deleteById(1);

        assertDoesNotThrow(() -> sensorTypeService.deleteSensorType(1));

        verify(repository, times(1)).existsById(1);
        verify(repository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteSensorTypeNotFound() {
        when(repository.existsById(999)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> sensorTypeService.deleteSensorType(999));
        assertTrue(exception.getMessage().contains("SensorType not found with id 999"));
        verify(repository, times(1)).existsById(999);
        verify(repository, never()).deleteById(anyInt());
    }
}
