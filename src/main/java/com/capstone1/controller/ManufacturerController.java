package com.capstone1.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.capstone1.model.Manufacturer;
import com.capstone1.services.CommonService;
import com.capstone1.services.ManufacturerService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;

@Controller
public class ManufacturerController {

    @Resource
    CommonService commonService;
    @Resource
    ManufacturerService manufacturerService;

    @GetMapping("/manufacturers")
    public String listManufacturers(Model model, HttpSession session) {
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
        List<Manufacturer> listManufacturers = manufacturerService.getAll();

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
    public String createManufacturer(Model model, HttpSession session) {
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
        Manufacturer manufacturer = new Manufacturer();
        model.addAttribute("manufacturer", manufacturer);
        return "manufacturers/create_manufacturer";
    }

    @PostMapping("/manufacturers/update-manufacturer/{id}")
    public String updateManufacturer(@PathVariable Long id, Model model,
            @RequestParam("manufacturerImg") MultipartFile file,
            @ModelAttribute Manufacturer manufacturer, HttpSession session) {
        // get Manufacturer exist
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
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
        return listManufacturers(model, session);
    }

    @PostMapping("/manufacturers/save-manufacturer")
    public String saveManufacturer(Model model, @RequestParam("manufacturerImg") MultipartFile file,
            @ModelAttribute Manufacturer manufacturer, HttpSession session) {
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
        try {
            manufacturer = manufacturerService.saveManufacturer(manufacturer);

            String fileName = manufacturer.getId() + ".png";
            String uploadDir = "manufacturer-upload/";

            manufacturer.setImages("/manufacturer-upload/" + fileName);
            manufacturer.setStatus(1);
            manufacturerService.saveManufacturer(manufacturer);

            saveFile(uploadDir, fileName, file);

            System.out.println("Manufacturer added successfully");
            model.addAttribute("alert", "success");

        } catch (Exception e) {
            model.addAttribute("alert", "error");
        }
        return listManufacturers(model, session);
    }

    @GetMapping("/manufacturers/change-status/{id}")
    public String changeStatus(@PathVariable Long id, Model model,
            @ModelAttribute Manufacturer manufacturer, HttpSession session) {
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
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

        return listManufacturers(model, session);
    }

    @GetMapping("/manufacturers/edit/{id}")
    public String editManufacturer(@PathVariable Long id, Model model, HttpSession session) {
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
        Manufacturer manufacturer = manufacturerService.getManufacturerById(id);
        model.addAttribute("manufacturer", manufacturer);
        return "manufacturers/edit_manufacturer";
    }

    @GetMapping("/manufacturers/delete-manufacturer/{id}")
    public String deleteManufacturer(@PathVariable Long id, HttpSession session, Model model) {
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
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
