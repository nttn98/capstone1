package com.capstone1.services;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.capstone1.model.Manufacturer;

public interface ManufacturerService {

    List<Manufacturer> getAll();

    Page<Manufacturer> getAllManufacturers(Pageable p);

    Manufacturer saveManufacturer(Manufacturer manufacturer);

    Manufacturer getManufacturerById(Long id);

    Manufacturer updateManufacturer(Manufacturer manufacturer);

    Manufacturer changeStatusManufacturer(Manufacturer manufacturer);

    void deleteManufacturerById(Long Id);

    Manufacturer findByName(String name);

}
