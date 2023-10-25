package com.capstone1.controller;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.capstone1.model.Manufacturer;
import com.capstone1.services.ManufacturerService;

@Controller
public class ManufacturerController {

    private ManufacturerService manufacturerService;

    public ManufacturerController(ManufacturerService manufacturerService) {
        super();
        this.manufacturerService = manufacturerService;
    }

    @GetMapping("/manufacturers")
    public String listManufacturers(Model model) {
        List<Manufacturer> listManufacturers = manufacturerService.getAllManufacturers();
        model.addAttribute("manufacturers", listManufacturers);
        return "manufacturers/manufacturers";
    }

    @GetMapping("/manufacturers/createManufacturer")
    public String createManufacturer(Model model) {
        Manufacturer manufacturer = new Manufacturer();
        model.addAttribute("manufacturer", manufacturer);
        return "manufacturers/create_manufacturer";
    }

    @PostMapping("/manufacturers/saveManufacturer")
    public String saveManufacturer(Model model, @RequestParam("manufacturerImg") MultipartFile file,
            @ModelAttribute("manufacturer") Manufacturer manufacturer) {
        try {
            manufacturer = manufacturerService.saveManufacturer(manufacturer);

            String fileName = manufacturer.getManufacturerId() + ".png";
            String uploadDir = "manufacturer-upload/";

            manufacturer.setManufacturerImages("/manufacturer-upload/" + fileName);
            manufacturerService.saveManufacturer(manufacturer);

            saveFile(uploadDir, fileName, file);

            System.out.println("Manufacturer added successfully");

        } catch (Exception e) {
            System.out.println(e);
        }
        return "redirect:/manufacturers";
    }

    @GetMapping("/manufacturers/edit/{id}")
    public String editManufacturer(@PathVariable Long id, Model model) {
        Manufacturer manufacturer = manufacturerService.getManufacturerById(id);
        model.addAttribute("manufacturer", manufacturer);
        return "manufacturers/edit_manufacturer";
    }

    @GetMapping("/manufacturers/deleteManufacturer/{id}")
    public String deleteManufacturer(@PathVariable Long id) {
        manufacturerService.deleteManufacturerById(id);
        return "redirect:/manufacturers";
    }

    /* SAVE METHOD */
    private void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {

        String orgName = multipartFile.getOriginalFilename();

        if (orgName != "") {
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = multipartFile.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println(
                        "---------------Manufacturers----------------------------"
                                + filePath.toAbsolutePath().toString());
            } catch (IOException ioe) {
                throw new IOException("Could not save image file: " + fileName, ioe);
            }
        }
    }
}
