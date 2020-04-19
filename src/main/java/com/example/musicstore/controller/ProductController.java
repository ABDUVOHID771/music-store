package com.example.musicstore.controller;

import com.example.musicstore.dto.ProductDto;
import com.example.musicstore.service.ProductService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String homePage() {
        return "home";
    }

    @GetMapping("/about")
    public String aboutUs() {
        return "about";
    }

    @GetMapping("/productList")
    public String productList(Model model) {
        List<ProductDto> products = productService.getAll();
//        List<String> images = products.stream().map(productDto ->
//                Base64.getEncoder().encodeToString(productDto.getProductImage())).collect(Collectors.toList());
//        model.addAttribute("images", images);
        model.addAttribute("products", products);
        return "productList";
    }

    @GetMapping("/productList/{productUUID}")
    public String productDetails(@PathVariable UUID productUUID, Model model) throws NotFoundException {
        ProductDto productDto = productService.getByUUID(productUUID);
//        model.addAttribute("productImage", Base64.getEncoder().encodeToString(productDto.getProductImage()));
        model.addAttribute("product", productDto);
        return "productDetails";
    }

    @GetMapping("/productList/search")
    public String getProductByCategory(@RequestParam("searchCondition") String searchCondition, Model model) {
        List<ProductDto> productDtos = productService.getAll();
        model.addAttribute("products", productDtos);
        model.addAttribute("searchCondition", searchCondition);
        return "productList";
    }

}
