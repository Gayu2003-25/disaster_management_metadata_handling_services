package com.kernelx.metadatahandling.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kernelx.metadatahandling.entity.SensorType;
import com.kernelx.metadatahandling.repository.SensorTypeRepository;
import com.kernelx.metadatahandling.security.JwtAuthenticationFilter;
import com.kernelx.metadatahandling.service.SensorTypeService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SensorTypeController.class)
@AutoConfigureMockMvc(addFilters = false)
class SensorTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SensorTypeService sensorTypeService;

    @MockitoBean
    private SensorTypeRepository sensorTypeRepository;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private ObjectMapper objectMapper = new ObjectMapper();
    private SensorType sensorType;

    @BeforeEach
    void setUp() {
        sensorType = new SensorType();
        sensorType.setSensorTypeId(1);
        sensorType.setType("Rainfall");
    }

    @Test
    void testGetAllTypes() throws Exception {
        when(sensorTypeRepository.findAll()).thenReturn(Arrays.asList(sensorType));

        mockMvc.perform(get("/sensor-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].type").value("Rainfall"));
    }

    @Test
    void testGetSensorTypeByIdSuccess() throws Exception {
        when(sensorTypeRepository.findById(1)).thenReturn(Optional.of(sensorType));

        mockMvc.perform(get("/sensor-types/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("Rainfall"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateType() throws Exception {
        when(sensorTypeRepository.save(any(SensorType.class))).thenReturn(sensorType);

        mockMvc.perform(post("/sensor-types")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sensorType)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("Rainfall"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateSensorType() throws Exception {
        when(sensorTypeService.updateSensorType(eq(1), any(SensorType.class))).thenReturn(sensorType);

        mockMvc.perform(put("/sensor-types/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sensorType)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("Rainfall"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteSensorType() throws Exception {
        doNothing().when(sensorTypeService).deleteSensorType(1);

        mockMvc.perform(delete("/sensor-types/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(sensorTypeService, times(1)).deleteSensorType(1);
    }
}
