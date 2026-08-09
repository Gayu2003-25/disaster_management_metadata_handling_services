package com.kernelx.metadatahandling.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kernelx.metadatahandling.entity.Site;
import com.kernelx.metadatahandling.repository.SiteRepository;
import com.kernelx.metadatahandling.security.JwtAuthenticationFilter;
import com.kernelx.metadatahandling.service.SiteService;
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

@WebMvcTest(SiteController.class)
@AutoConfigureMockMvc(addFilters = false)
class SiteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SiteService siteService;

    @MockitoBean
    private SiteRepository siteRepository;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Site site;

    @BeforeEach
    void setUp() {
        site = new Site();
        site.setSiteId(1);
        site.setLocation("Colombo");
        site.setSiteName("Main Station");
    }

    @Test
    void testGetAllSites() throws Exception {
        when(siteRepository.findAll()).thenReturn(Arrays.asList(site));

        mockMvc.perform(get("/sites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].siteName").value("Main Station"));
    }

    @Test
    void testGetSiteByIdSuccess() throws Exception {
        when(siteRepository.findById(1)).thenReturn(Optional.of(site));

        mockMvc.perform(get("/sites/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.siteName").value("Main Station"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateSite() throws Exception {
        when(siteRepository.save(any(Site.class))).thenReturn(site);

        mockMvc.perform(post("/sites")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(site)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.siteName").value("Main Station"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateSite() throws Exception {
        when(siteService.updateSite(eq(1), any(Site.class))).thenReturn(site);

        mockMvc.perform(put("/sites/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(site)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.siteName").value("Main Station"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteSite() throws Exception {
        doNothing().when(siteService).deleteSite(1);

        mockMvc.perform(delete("/sites/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(siteService, times(1)).deleteSite(1);
    }
}
