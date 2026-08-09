package com.kernelx.metadatahandling.service;

import com.kernelx.metadatahandling.entity.Site;
import com.kernelx.metadatahandling.repository.SiteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SiteServiceTest {

    @Mock
    private SiteRepository repository;

    @InjectMocks
    private SiteService siteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateSiteSuccess() {
        Site existingSite = new Site();
        existingSite.setSiteId(1);
        existingSite.setLocation("Colombo");
        existingSite.setSiteName("Main Station");

        Site updatedDetails = new Site();
        updatedDetails.setLocation("Kandy");
        updatedDetails.setSiteName("Kandy Station");

        when(repository.findById(1)).thenReturn(Optional.of(existingSite));
        when(repository.save(any(Site.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Site result = siteService.updateSite(1, updatedDetails);

        assertNotNull(result);
        assertEquals("Kandy", result.getLocation());
        assertEquals("Kandy Station", result.getSiteName());
        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(existingSite);
    }

    @Test
    void testUpdateSiteNotFound() {
        Site updatedDetails = new Site();
        updatedDetails.setLocation("Galle");
        updatedDetails.setSiteName("Galle Station");

        when(repository.findById(999)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> siteService.updateSite(999, updatedDetails));
        assertTrue(exception.getMessage().contains("Site not found with id 999"));
        verify(repository, times(1)).findById(999);
        verify(repository, never()).save(any());
    }

    @Test
    void testDeleteSiteSuccess() {
        when(repository.existsById(1)).thenReturn(true);
        doNothing().when(repository).deleteById(1);

        assertDoesNotThrow(() -> siteService.deleteSite(1));

        verify(repository, times(1)).existsById(1);
        verify(repository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteSiteNotFound() {
        when(repository.existsById(999)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> siteService.deleteSite(999));
        assertTrue(exception.getMessage().contains("Site not found with id 999"));
        verify(repository, times(1)).existsById(999);
        verify(repository, never()).deleteById(anyInt());
    }
}
