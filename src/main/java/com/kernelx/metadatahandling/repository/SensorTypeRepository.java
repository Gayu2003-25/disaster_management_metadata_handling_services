package com.kernelx.metadatahandling.repository;

import com.kernelx.metadatahandling.entity.SensorType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorTypeRepository extends JpaRepository<SensorType, Integer> {
}
