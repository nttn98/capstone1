package com.capstone1.controller;

import java.io.*;
import java.nio.file.*;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.capstone1.model.Category;
import com.capstone1.model.Manufacturer;
import com.capstone1.model.Product;
import com.capstone1.services.CategoryService;
import com.capstone1.services.ManufacturerService;
import com.capstone1.services.ProductService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;

@Controller
public class ProductController {

    @Resource
    ProductService productService;
    @Resource

    CategoryService categoryService;
    @Resource
    ManufacturerService manufacturerService;

    @Autowired
    HomeController homeController;

    @GetMapping("/products")
    public String listProducts(Model model, HttpSession session, @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size) {
        homeController.isLogin(model, session);
        Page<Product> listProducts = productService.getAllProducts(PageRequest.of(page, size));
        // List<Category> listCategories = categoryService.getAllCategories();
        // List<Manufacturer> listManufacturers =
        // manufacturerService.getAllManufacturers();
        // model.addAttribute("categories", listCategories);
        // model.addAttribute("manufacturers", listManufacturers);

        if (listProducts.isEmpty()) {
            Product product = new Product();

            model.addAttribute("product", product);

            return createProductForm(model);
        } else {
            model.addAttribute("products", listProducts);
        }
        return "products/products";
    }

    @GetMapping("/products/create-product")
    public String createProductForm(Model model) {
        Product product = new Product();
        List<Category> listCategories = categoryService.getAll();
        List<Manufacturer> listManufacturers = manufacturerService.getAll();

        model.addAttribute("manufacturers", listManufacturers);
        model.addAttribute("categories", listCategories);
        model.addAttribute("product", product);

        return "products/create_product";

    }

    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        model.addAttribute("manufacturers", manufacturerService.getAll());
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("product", productService.getProductById(id));
        return "products/edit_product";
    }

    @PostMapping("/products/update-product/{id}")
    public String updateProduct(@PathVariable Long id, Model model, @RequestParam("productImg") MultipartFile file,
                                @ModelAttribute Product product, HttpSession session, @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "5") int size) {
        // get product exist
        Product existProduct = productService.getProductById(id);

        existProduct.setName(product.getName());
        existProduct.setPrice(product.getPrice());
        existProduct.setQuantity(product.getQuantity());
        existProduct.setCategory(product.getCategory());
        existProduct.setDescription(product.getDescription());
        existProduct.setManufacturer(product.getManufacturer());

        try {
            String fileName = existProduct.getId() + ".png";
            String uploadDir = "product-upload/";

            existProduct.setImages("/product-upload/" + fileName);

            saveFile(uploadDir, fileName, file);

            System.out.println("Product edited successfully.");

            model.addAttribute("alert", "edit");
        } catch (Exception e) {
            model.addAttribute("alert", "error");
        }

        // save updated
        productService.updateProduct(existProduct);

        return listProducts(model, session, page, size);
    }

    @GetMapping("/products/change-status/{id}")
    public String changeStatus(@PathVariable Long id, Model model, @ModelAttribute Product product, HttpSession session, @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size) {

        // get product exist
        Product existProduct = productService.getProductById(id);

        if (existProduct.getStatus() == 0) {
            existProduct.setStatus(1);
        } else {
            existProduct.setStatus(0);
        }
        model.addAttribute("alert", "edit");

        // save updated
        productService.updateProduct(existProduct);

        return listProducts(model, session, page, size);
    }

    @GetMapping("/products/change-newest/{id}")
    public String changeNewest(@PathVariable Long id, Model model, @ModelAttribute Product product, HttpSession session, @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size) {

        // get product exist
        Product existProduct = productService.getProductById(id);

        if (existProduct.getIsNewest() == 1) {
            existProduct.setIsNewest(0);
        } else {
            existProduct.setIsNewest(1);
        }
        model.addAttribute("alert", "edit");
        // save updated
        productService.updateProduct(existProduct);

        return listProducts(model, session, page, size);
    }

    @GetMapping("/products/delete-product/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:/products";
    }

    /* Create Product */
    @PostMapping("/products/save-product")
    public String saveProduct(Model model, @RequestParam("productImg") MultipartFile file,
                              @ModelAttribute Product product, @RequestParam long quantity, HttpSession session,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "5") int size) {

        try {
            product.setQuantity(quantity);
            product = productService.saveProduct(product);

            String fileName = product.getId() + ".png";
            String uploadDir = "product-upload/";

            product.setImages("/product-upload/" + fileName);
            productService.saveProduct(product);

            saveFile(uploadDir, fileName, file);

            System.out.println("Product added successfully.");
            model.addAttribute("alert", "success");
        } catch (Exception e) {
            model.addAttribute("alert", "error");

        }
        return listProducts(model, session, page, size);
    }

    /*Search product*/
    @PostMapping("/products/search")
    public String searchProduct(Model model, HttpSession session, @RequestParam("keywords") String keywords, @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "5") int size) {
//        System.out.println(keywords);
        Page<Product> foundProducts = productService.findByNameContaining(keywords, PageRequest.of(page, size));
        List<Category> categories = categoryService.getAll();
        List<Manufacturer> manufacturers = manufacturerService.getAll();

        model.addAttribute("categories", categories);
        model.addAttribute("manufacturers", manufacturers);
        model.addAttribute("products", foundProducts);
        session.setAttribute("keywords", keywords);

//        System.out.println(foundProducts.toList().size());

        return "searchingPage";
    }

    @GetMapping("/searchPaging")
    public String paginated(Model model, HttpSession session, @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size) {
        Page<Product> products = null;

        String keywords = (String) session.getAttribute("keywords");

        products = productService.findByNameContaining(keywords, PageRequest.of(page, size));


        model.addAttribute("products", products);

        List<Category> categories = categoryService.getAll();
        List<Manufacturer> manufacturers = manufacturerService.getAll();

        // model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("manufacturers", manufacturers);

        return "searchingPage";
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
                System.out.println("-------------Products-----------------" + filePath.toAbsolutePath().toString());
            } catch (IOException ioe) {
                throw new IOException("Could not save image file: " + fileName, ioe);
            }
        }
    }

    @GetMapping("/checkProductNameAvailability")
    @ResponseBody // Ensure the returned boolean is serialized as a response body
    public ResponseEntity<Boolean> checkProductNameAvailability(@RequestParam("name") String name) {
        try {
            Product productExist = productService.findByName(name);
            if (productExist == null) {
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
