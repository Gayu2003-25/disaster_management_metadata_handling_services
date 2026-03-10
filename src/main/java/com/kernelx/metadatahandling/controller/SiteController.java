package com.kernelx.metadatahandling.controller;

import com.kernelx.metadatahandling.entity.Site;
import com.kernelx.metadatahandling.service.SiteService;
import org.springframework.web.bind.annotation.*;

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
}
