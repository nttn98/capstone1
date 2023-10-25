package com.capstone1.model;

import jakarta.persistence.*;

@Entity
@Table(name = "manufacturers")
public class Manufacturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long manufacturerId;
    private String manufacturerName;
    private String manufacturerImages;
    private String manufacturerDescription;

    public Manufacturer() {
        super();
    }

    public long getManufacturerId() {
        return manufacturerId;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public String getManufacturerImages() {
        return manufacturerImages;
    }

    public String getManufacturerDescription() {
        return manufacturerDescription;
    }

    public void setManufacturerName(String manufactureName) {
        this.manufacturerName = manufactureName;
    }

    public void setManufacturerImages(String manufactureImages) {
        this.manufacturerImages = manufactureImages;
    }

    public void setManufacturerDescription(String manufactureDescription) {
        this.manufacturerDescription = manufactureDescription;
    }

}
