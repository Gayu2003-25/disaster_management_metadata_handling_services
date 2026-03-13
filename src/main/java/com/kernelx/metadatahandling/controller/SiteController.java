package com.kernelx.metadatahandling.controller;

import com.kernelx.metadatahandling.entity.Site;
import com.kernelx.metadatahandling.repository.SiteRepository;
import com.kernelx.metadatahandling.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sites")
public class SiteController {

    private final SiteService service;

    public SiteController(SiteService service) {
        this.service = service;
    }

    @PutMapping("/{id}")
    public Site updateSite(@PathVariable int id, @RequestBody Site siteDetails) {
        return service.updateSite(id, siteDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteSite(@PathVariable int id) {
        service.deleteSite(id);
    }

    @Autowired
    private SiteRepository siteRepository;

    // Create a new Site
    @PostMapping
    public Site createSite(@RequestBody Site site) {
        return siteRepository.save(site);
    }

    // View all Sites
    @GetMapping
    public List<Site> getAllSites() {
        return siteRepository.findAll();
    }

    // View Site by ID
    @GetMapping("/{id}")
    public Site getSiteById(@PathVariable Integer id) {
        return siteRepository.findById(id).orElse(null);
    }
}
