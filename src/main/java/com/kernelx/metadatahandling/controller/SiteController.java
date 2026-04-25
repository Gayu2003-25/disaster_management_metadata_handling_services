package com.kernelx.metadatahandling.controller;

import com.kernelx.metadatahandling.entity.Site;
import com.kernelx.metadatahandling.repository.SiteRepository;
import com.kernelx.metadatahandling.service.SiteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/sites")
public class SiteController {

    // 1. You must declare the Service
    private final SiteService service;

    // 2. You must declare the Repository
    @Autowired
    private SiteRepository siteRepository;

    // 3. Constructor to initialize the Service
    public SiteController(SiteService service) {
        this.service = service;
    }

    // View all Sites: Returns 200 OK
    @GetMapping
    public ResponseEntity<List<Site>> getAllSites() {
        List<Site> sites = siteRepository.findAll();
        log.info("HTTP STATUS: 200 OK - Fetched {} sites", sites.size());
        return ResponseEntity.ok(sites);
    }

    // View Site by ID: Returns 200 OK
    @GetMapping("/{id}")
    public ResponseEntity<Site> getSiteById(@PathVariable Integer id) {
        Site site = siteRepository.findById(id).orElseThrow(() -> {
            // This logs the 404 error to your terminal
            log.error("HTTP STATUS: 404 NOT FOUND - Site ID {} not found", id);
            return new RuntimeException("Site not found with id " + id);
        });
        log.info("HTTP STATUS: 200 OK - Fetched Site ID: {}", id);
        return ResponseEntity.ok(site);
    }

    // Create a new Site: Returns 201 Created
    @PostMapping
    public ResponseEntity<Site> createSite(@RequestBody Site site) {
        Site saved = siteRepository.save(site);
        log.info("HTTP STATUS: 201 CREATED - Created Site: {}", saved.getSiteName());
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Update Site: Returns 200 OK
    @PutMapping("/{id}")
    public ResponseEntity<Site> updateSite(@PathVariable int id, @RequestBody Site details) {
        Site updated = service.updateSite(id, details);
        log.info("HTTP STATUS: 200 OK - Updated Site ID: {}", id);
        return ResponseEntity.ok(updated);
    }

    // Delete Site: Returns 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSite(@PathVariable int id) {
        service.deleteSite(id);
        log.info("HTTP STATUS: 204 NO CONTENT - Deleted Site ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}