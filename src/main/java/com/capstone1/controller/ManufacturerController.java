package com.capstone1.controller;

import java.io.*;
import java.nio.file.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.capstone1.model.Manufacturer;
import com.capstone1.services.ManufacturerService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;

@Controller
public class ManufacturerController {

    @Resource
    ManufacturerService manufacturerService;

    @Autowired
    HomeController homeController;

    @GetMapping("/manufacturers")
    public String listManufacturers(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size, Model model, HttpSession session) {
        String target = homeController.isLogin(model, session);
        if(target!=null){
            return target;
        }
        Page<Manufacturer> listManufacturers = manufacturerService.getAllManufacturers(PageRequest.of(page, size));

        if (listManufacturers.isEmpty()) {
            Manufacturer manufacturer = new Manufacturer();

            model.addAttribute("manufacturer", manufacturer);
            return "manufacturers/create_manufacturer";
        } else {
            model.addAttribute("manufacturers", listManufacturers);
            return "manufacturers/manufacturers";
        }
    }

    @GetMapping("/manufacturers/create-manufacturer")
    public String createManufacturer(Model model) {
        Manufacturer manufacturer = new Manufacturer();
        model.addAttribute("manufacturer", manufacturer);
        return "manufacturers/create_manufacturer";
    }

    @PostMapping("/manufacturers/update-manufacturer/{id}")
    public String updateManufacturer(@PathVariable Long id, Model model,
                                     @RequestParam("manufacturerImg") MultipartFile file,
                                     @ModelAttribute Manufacturer manufacturer, HttpSession session, @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        // get Manufacturer exist
        Manufacturer existManufacturer = manufacturerService.getManufacturerById(id);

        existManufacturer.setName(manufacturer.getName());
        existManufacturer.setDescription(manufacturer.getDescription());


        try {
            String fileName = existManufacturer.getId() + ".png";
            String uploadDir = "manufacturer-upload/";

            existManufacturer.setImages("/manufacturer-upload/" + fileName);

            saveFile(uploadDir, fileName, file);

            System.out.println("Manufacturer edited successfully.");
            model.addAttribute("alert", "edit");

        } catch (Exception e) {
            System.out.println(e);
        }
        // save updated
        manufacturerService.updateManufacturer(existManufacturer);
        return listManufacturers(page, size, model, session);
    }

    @PostMapping("/manufacturers/save-manufacturer")
    public String saveManufacturer(Model model, @RequestParam("manufacturerImg") MultipartFile file,
                                   @ModelAttribute Manufacturer manufacturer, HttpSession session, @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {

        try {
            manufacturer = manufacturerService.saveManufacturer(manufacturer);

            String fileName = manufacturer.getId() + ".png";
            String uploadDir = "manufacturer-upload/";

            manufacturer.setImages("/manufacturer-upload/" + fileName);
            manufacturerService.saveManufacturer(manufacturer);

            saveFile(uploadDir, fileName, file);

            System.out.println("Manufacturer added successfully");
            model.addAttribute("alert", "success");

        } catch (Exception e) {
            model.addAttribute("alert", "error");
        }
        return listManufacturers(page, size, model, session);
    }

    @GetMapping("/manufacturers/change-status/{id}")
    public String changeStatus(@PathVariable Long id, Model model,
                               @ModelAttribute Manufacturer manufacturer, HttpSession session, @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {

        // get product exist
        Manufacturer existManufacturer = manufacturerService.getManufacturerById(id);

        if (existManufacturer.getStatus() == 0) {
            existManufacturer.setStatus(1);
        } else {
            existManufacturer.setStatus(0);
        }
        model.addAttribute("alert", "edit");
        // save updated
        manufacturerService.updateManufacturer(existManufacturer);

        return listManufacturers(page, size, model, session);
    }

    @GetMapping("/manufacturers/edit/{id}")
    public String editManufacturer(@PathVariable Long id, Model model) {
        Manufacturer manufacturer = manufacturerService.getManufacturerById(id);
        model.addAttribute("manufacturer", manufacturer);
        return "manufacturers/edit_manufacturer";
    }

    @GetMapping("/manufacturers/delete-manufacturer/{id}")
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

    @GetMapping("/checkManuFactureNameAvailability")
    @ResponseBody // Ensure the returned boolean is serialized as a response body
    public ResponseEntity<Boolean> checkManuFactureNameAvailability(@RequestParam("name") String name) {
        try {
            Manufacturer manufacturerExist = manufacturerService.findByName(name);
            if (manufacturerExist == null) {
                return ResponseEntity.ok(true); // name is available
            } else {
                return ResponseEntity.ok(false); // name is not available
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
}
