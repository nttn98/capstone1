package com.capstone1.services;

import java.util.*;

import com.capstone1.model.Manufacturer;

public interface ManufacturerService {

    List<Manufacturer> getAllManufacturers();

    Manufacturer saveManufacturer(Manufacturer manufacturer);

    Manufacturer getManufacturerById(Long id);

    Manufacturer updateManufacturer(Manufacturer manufacturer);

    void deleteManufacturerById(Long Id);

}
