package com.kernelx.metadatahandling.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sensors")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "sensor_type_id")
    private SensorType sensorType;

    @ManyToOne
    @JoinColumn(name = "site_id")
    private Site site;

    private double latitude;
    private double longitude;
    private String unitOfMeasure;

    private double thresholdHighWarning;
    private double thresholdHighCritical;
    private double thresholdLowWarning;
    private double thresholdLowCritical;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public double getThresholdHighWarning() {
        return thresholdHighWarning;
    }

    public void setThresholdHighWarning(double thresholdHighWarning) {
        this.thresholdHighWarning = thresholdHighWarning;
    }

    public double getThresholdHighCritical() {
        return thresholdHighCritical;
    }

    public void setThresholdHighCritical(double thresholdHighCritical) {
        this.thresholdHighCritical = thresholdHighCritical;
    }

    public double getThresholdLowWarning() {
        return thresholdLowWarning;
    }

    public void setThresholdLowWarning(double thresholdLowWarning) {
        this.thresholdLowWarning = thresholdLowWarning;
    }

    public double getThresholdLowCritical() {
        return thresholdLowCritical;
    }

    public void setThresholdLowCritical(double thresholdLowCritical) {
        this.thresholdLowCritical = thresholdLowCritical;
    }
}