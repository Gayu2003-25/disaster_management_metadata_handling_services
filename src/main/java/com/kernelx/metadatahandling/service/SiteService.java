package com.kernelx.metadatahandling.service;

import com.kernelx.metadatahandling.entity.Site;
import com.kernelx.metadatahandling.repository.SiteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SiteService {

    private final SiteRepository repository;

    public SiteService(SiteRepository repository) {
        this.repository = repository;
    }

    public Site updateSite(int id, Site siteDetails) {

        Site site = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Site not found with id " + id));

        site.setGlobalSiteId(siteDetails.getGlobalSiteId());
        site.setLocation(siteDetails.getLocation());
        site.setSiteName(siteDetails.getSiteName());

        return repository.save(site);
    }

    public void deleteSite(int id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException("Site not found with id " + id);
        }

        repository.deleteById(id);
    }
}