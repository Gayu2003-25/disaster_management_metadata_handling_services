package com.kernelx.metadatahandling.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kernelx.metadatahandling.entity.Sensor;
import com.kernelx.metadatahandling.entity.SensorType;
import com.kernelx.metadatahandling.entity.Site;
import com.kernelx.metadatahandling.security.JwtAuthenticationFilter;
import com.kernelx.metadatahandling.service.SensorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SensorController.class)
@AutoConfigureMockMvc(addFilters = false)
class SensorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SensorService sensorService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Sensor sensor;

    @BeforeEach
    void setUp() {
        Site site = new Site();
        site.setSiteId(1);
        site.setSiteName("Main Station");

        SensorType type = new SensorType();
        type.setSensorTypeId(1);
        type.setType("Water Level");

        sensor = new Sensor();
        sensor.setSensorId("sensor-001");
        sensor.setSite(site);
        sensor.setSensorType(type);
        sensor.setLatitude(6.9271);
        sensor.setLongitude(79.8612);
        sensor.setUnitOfMeasure("m");
        sensor.setThresholdHighWarning(5.0);
        sensor.setThresholdHighCritical(8.0);
        sensor.setThresholdLowWarning(1.0);
        sensor.setThresholdLowCritical(0.5);
    }

    @Test
    void testGetAllSensors() throws Exception {
        when(sensorService.getAllSensors()).thenReturn(Arrays.asList(sensor));

        mockMvc.perform(get("/sensors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].sensorId").value("sensor-001"));
    }

    @Test
    void testGetSensorById() throws Exception {
        when(sensorService.getSensorById("sensor-001")).thenReturn(sensor);

        mockMvc.perform(get("/sensors/sensor-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sensorId").value("sensor-001"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateSensor() throws Exception {
        when(sensorService.createSensor(any(Sensor.class))).thenReturn(sensor);

        mockMvc.perform(post("/sensors")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sensor)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sensorId").value("sensor-001"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateSensor() throws Exception {
        when(sensorService.updateSensor(eq("sensor-001"), any(Sensor.class))).thenReturn(sensor);

        mockMvc.perform(put("/sensors/sensor-001")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sensor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sensorId").value("sensor-001"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteSensor() throws Exception {
        doNothing().when(sensorService).deleteSensor("sensor-001");

        mockMvc.perform(delete("/sensors/sensor-001").with(csrf()))
                .andExpect(status().isNoContent());

        verify(sensorService, times(1)).deleteSensor("sensor-001");
    }
}
