package com.capstone1.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import java.util.List;

import com.capstone1.model.Manufacturer;
import com.capstone1.repository.ManufacturerRepository;
import com.capstone1.services.ManufacturerService;

@Service
public class Manufacturerimpl implements ManufacturerService {

    @Autowired
    ManufacturerRepository manufacturerRepository;

    @Override
    public List<Manufacturer> getAllManufacturers() {
        return manufacturerRepository.findAll();
    }

    @Override

    public Manufacturer saveManufacturer(Manufacturer manufacturer) {
        // manufacturerRepository.alterAutoIncrementValue();
        return manufacturerRepository.save(manufacturer);
    }

    @Override
    public Manufacturer getManufacturerById(Long id) {
        return manufacturerRepository.findById(id).get();
    }

    @Override
    public Manufacturer updateManufacturer(Manufacturer manufacturer) {
        return manufacturerRepository.save(manufacturer);
    }

    @Override
    public void deleteManufacturerById(Long Id) {
        manufacturerRepository.deleteById(Id);
    }

    @Override
    public Manufacturer changeStatusManufacturer(Manufacturer manufacturer) {
        return manufacturerRepository.save(manufacturer);
    }

    @Override
    public Manufacturer findByName(String name) {
        return manufacturerRepository.findByName(name);
    }

}
