package com.kernelx.metadatahandling.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;

@Entity
@Table(name = "sensor")
@JsonPropertyOrder({ "sensorId", "sensorType", "site", "latitude", "longitude" })
public class Sensor {

    @Id
    @Column(name = "sensor_id")
    private Integer sensorId;

    @ManyToOne
    @JoinColumn(name = "sensor_type_id", nullable = false)
    private SensorType sensorType;

    @ManyToOne
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(name = "unit_of_measure", nullable = false, length = 8)
    private String unitOfMeasure;

    @Column(name = "threshold_high_warning", nullable = false)
    private double thresholdHighWarning;

    @Column(name = "threshold_high_critical", nullable = false)
    private double thresholdHighCritical;

    @Column(name = "threshold_low_warning", nullable = false)
    private double thresholdLowWarning;

    @Column(name = "threshold_low_critical", nullable = false)
    private double thresholdLowCritical;

    // GETTERS AND SETTERS

    public Integer getSensorId() { return sensorId; }
    public void setSensorId(Integer sensorId) { this.sensorId = sensorId; }

    public SensorType getSensorType() { return sensorType; }
    public void setSensorType(SensorType sensorType) { this.sensorType = sensorType; }

    public Site getSite() { return site; }
    public void setSite(Site site) { this.site = site; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getUnitOfMeasure() { return unitOfMeasure; }
    public void setUnitOfMeasure(String unitOfMeasure) { this.unitOfMeasure = unitOfMeasure; }

    public double getThresholdHighWarning() { return thresholdHighWarning; }
    public void setThresholdHighWarning(double thresholdHighWarning) { this.thresholdHighWarning = thresholdHighWarning; }

    public double getThresholdHighCritical() { return thresholdHighCritical; }
    public void setThresholdHighCritical(double thresholdHighCritical) { this.thresholdHighCritical = thresholdHighCritical; }

    public double getThresholdLowWarning() { return thresholdLowWarning; }
    public void setThresholdLowWarning(double thresholdLowWarning) { this.thresholdLowWarning = thresholdLowWarning; }

    public double getThresholdLowCritical() { return thresholdLowCritical; }
    public void setThresholdLowCritical(double thresholdLowCritical) { this.thresholdLowCritical = thresholdLowCritical; }
}