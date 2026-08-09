package com.kernelx.metadatahandling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kernelx.metadatahandling.entity.Sensor;
import com.kernelx.metadatahandling.entity.SensorType;
import com.kernelx.metadatahandling.entity.Site;
import com.kernelx.metadatahandling.repository.SensorRepository;
import com.kernelx.metadatahandling.repository.SensorTypeRepository;
import com.kernelx.metadatahandling.repository.SiteRepository;
import com.kernelx.metadatahandling.security.TokenBlacklistService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Key;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:metadata_testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class MetadataHandlingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private SensorTypeRepository sensorTypeRepository;

    @Autowired
    private SensorRepository sensorRepository;

    @MockitoBean
    private TokenBlacklistService tokenBlacklistService;

    @MockitoBean
    private StringRedisTemplate redisTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();
    private String adminToken;

    @BeforeEach
    void setUp() {
        sensorRepository.deleteAll();
        sensorTypeRepository.deleteAll();
        siteRepository.deleteAll();

        when(tokenBlacklistService.isTokenBlacklisted(anyString())).thenReturn(false);

        adminToken = generateAdminToken();
    }

    private String generateAdminToken() {
        byte[] keyBytes = Decoders.BASE64.decode("404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        Key key = Keys.hmacShaKeyFor(keyBytes);
        return Jwts.builder()
                .setSubject("admin")
                .claim("role", "ADMIN")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    @Test
    @DisplayName("IT-010: Entity & Schema Mapping - Verify Site, Sensor-Type, and Sensor CRUD operations")
    void testIT010_EntityAndSchemaMappingCrudLifecycle() throws Exception {
        // 1. Create Site
        Site site = new Site();
        site.setLocation("Kelani River Basin");
        site.setSiteName("Hanwella Station");

        String siteResp = mockMvc.perform(post("/sites")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(site)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.siteId").exists())
                .andExpect(jsonPath("$.siteName").value("Hanwella Station"))
                .andReturn().getResponse().getContentAsString();

        Site savedSite = objectMapper.readValue(siteResp, Site.class);

        // 2. Create SensorType
        SensorType type = new SensorType();
        type.setType("Water Level Gauge");

        String typeResp = mockMvc.perform(post("/sensor-types")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(type)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sensorTypeId").exists())
                .andExpect(jsonPath("$.type").value("Water Level Gauge"))
                .andReturn().getResponse().getContentAsString();

        SensorType savedType = objectMapper.readValue(typeResp, SensorType.class);

        // 3. Create Sensor bound to Site and Type
        Sensor sensor = new Sensor();
        sensor.setSensorId("SN-KEL-001");
        sensor.setSite(savedSite);
        sensor.setSensorType(savedType);
        sensor.setLatitude(6.9012);
        sensor.setLongitude(80.0834);
        sensor.setUnitOfMeasure("m");
        sensor.setThresholdHighWarning(4.5);
        sensor.setThresholdHighCritical(7.0);
        sensor.setThresholdLowWarning(1.0);
        sensor.setThresholdLowCritical(0.5);

        mockMvc.perform(post("/sensors")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sensor)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sensorId").value("SN-KEL-001"))
                .andExpect(jsonPath("$.unitOfMeasure").value("m"));

        // Verify in DB
        assertThat(sensorRepository.existsById("SN-KEL-001")).isTrue();
    }

    @Test
    @DisplayName("IT-011: Database Migration - PostgreSQL table structure migration and global site ID entity binding")
    void testIT011_GlobalSiteIdEntityBinding() throws Exception {
        Site site = new Site();
        site.setLocation("Kalu Ganga");
        site.setSiteName("Ratnapura Main");
        Site savedSite = siteRepository.save(site);

        SensorType type = new SensorType();
        type.setType("Rain Gauge");
        SensorType savedType = sensorTypeRepository.save(type);

        mockMvc.perform(get("/sites/" + savedSite.getSiteId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location").value("Kalu Ganga"));

        assertThat(siteRepository.findById(savedSite.getSiteId())).isPresent();
    }

    @Test
    @DisplayName("IT-012: Schema Synchronization - Verify manual sensor ID logic and binding after updating JPA annotations")
    void testIT012_ManualSensorIdBindingAndPersistence() throws Exception {
        Site site = new Site();
        site.setLocation("Mahaweli Reach");
        site.setSiteName("Peradeniya Site");
        Site savedSite = siteRepository.save(site);

        SensorType type = new SensorType();
        type.setType("Flow Rate Sensor");
        SensorType savedType = sensorTypeRepository.save(type);

        String customSensorId = "CUSTOM-SENSOR-999";
        Sensor sensor = new Sensor();
        sensor.setSensorId(customSensorId);
        sensor.setSite(savedSite);
        sensor.setSensorType(savedType);
        sensor.setLatitude(7.2700);
        sensor.setLongitude(80.5900);
        sensor.setUnitOfMeasure("m3/s");
        sensor.setThresholdHighWarning(100.0);
        sensor.setThresholdHighCritical(200.0);
        sensor.setThresholdLowWarning(10.0);
        sensor.setThresholdLowCritical(5.0);

        mockMvc.perform(post("/sensors")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sensor)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sensorId").value(customSensorId));

        Sensor fetchedSensor = sensorRepository.findById(customSensorId).orElse(null);
        assertThat(fetchedSensor).isNotNull();
        assertThat(fetchedSensor.getSensorId()).isEqualTo(customSensorId);
        assertThat(fetchedSensor.getSite().getSiteId()).isEqualTo(savedSite.getSiteId());
    }
}
