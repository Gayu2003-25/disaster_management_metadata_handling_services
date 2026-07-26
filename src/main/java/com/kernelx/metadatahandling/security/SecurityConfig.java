package com.kernelx.metadatahandling.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    private static final String SENSORS_PATH = "/sensors/**";
    private static final String SENSOR_TYPES_PATH = "/sensor-types/**";
    private static final String SITES_PATH = "/sites/**";
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_OPERATOR = "OPERATOR";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            http
                    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            // GET requests allowed without auth (unrestricted)
                            .requestMatchers(HttpMethod.GET, SENSORS_PATH).permitAll()
                            .requestMatchers(HttpMethod.GET, SENSOR_TYPES_PATH).permitAll()
                            .requestMatchers(HttpMethod.GET, SITES_PATH).permitAll()
                            
                            // Swagger Endpoints
                            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                            
                            // Modifying requests restricted to ADMIN and OPERATOR
                            .requestMatchers(HttpMethod.POST, SENSORS_PATH).hasAnyRole(ROLE_ADMIN, ROLE_OPERATOR)
                            .requestMatchers(HttpMethod.PUT, SENSORS_PATH).hasAnyRole(ROLE_ADMIN, ROLE_OPERATOR)
                            .requestMatchers(HttpMethod.DELETE, SENSORS_PATH).hasAnyRole(ROLE_ADMIN, ROLE_OPERATOR)

                            .requestMatchers(HttpMethod.POST, SENSOR_TYPES_PATH).hasAnyRole(ROLE_ADMIN, ROLE_OPERATOR)
                            .requestMatchers(HttpMethod.PUT, SENSOR_TYPES_PATH).hasAnyRole(ROLE_ADMIN, ROLE_OPERATOR)
                            .requestMatchers(HttpMethod.DELETE, SENSOR_TYPES_PATH).hasAnyRole(ROLE_ADMIN, ROLE_OPERATOR)

                            .requestMatchers(HttpMethod.POST, SITES_PATH).hasAnyRole(ROLE_ADMIN, ROLE_OPERATOR)
                            .requestMatchers(HttpMethod.PUT, SITES_PATH).hasAnyRole(ROLE_ADMIN, ROLE_OPERATOR)
                            .requestMatchers(HttpMethod.DELETE, SITES_PATH).hasAnyRole(ROLE_ADMIN, ROLE_OPERATOR)

                            .anyRequest().authenticated()
                    )
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to build security filter chain", e);
        }
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:8090"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
