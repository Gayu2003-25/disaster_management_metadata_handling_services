package com.kernelx.metadatahandling;

import com.kernelx.metadatahandling.security.TokenBlacklistService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:app_testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class MetadatahandlingApplicationTests {

    @MockitoBean
    private TokenBlacklistService tokenBlacklistService;

    @MockitoBean
    private StringRedisTemplate redisTemplate;

    @Test
    void contextLoads() {
    }

}
