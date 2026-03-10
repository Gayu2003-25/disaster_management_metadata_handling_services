package com.kernelx.metadatahandling.repository;

import com.kernelx.metadatahandling.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteRepository extends JpaRepository<Site, Integer> {
}