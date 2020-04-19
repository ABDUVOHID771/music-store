package com.example.musicstore.controller;

import com.example.musicstore.dto.*;
import com.example.musicstore.service.*;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/admin")
@Controller
public class AdminController {

    private final ProductService productService;
    private final CustomerService customerService;
    private final CustomerOrderService customerOrderService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    private Path path;

    public AdminController(ProductService productService, CustomerService customerService, CustomerOrderService customerOrderService, UserService userService, PasswordEncoder passwordEncoder) {
        this.productService = productService;
        this.customerService = customerService;
        this.customerOrderService = customerOrderService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping({"", "/"})
    public String adminPage() {
        return "admin";
    }

    @GetMapping("/productInventory")
    public String productInventoryPage(Model model) {
        List<ProductDto> products = this.productService.getAll();
        model.addAttribute("products", products);
        return "productInventory";
    }

    @GetMapping("/productInventory/addProduct")
    public String addProductGet(Model model) {
        ProductDto productDto = new ProductDto();
        productDto.setProductCategory("instrument");
        productDto.setProductCondition("new");
        productDto.setProductStatus("active");
        model.addAttribute("product", productDto);
        return "addProduct";
    }

    @PostMapping("/productInventory/addProduct")
    public String addProductPost(@Valid @ModelAttribute("product") ProductDto productDto, BindingResult bindingResult, HttpServletRequest request) throws IOException, NotFoundException {
        log.info("Coming productDto is : {}", productDto);
        if (bindingResult.hasErrors())
            return "addProduct";

        ProductDto createdProduct = productService.create(productDto);

        MultipartFile productImage = productDto.getProductImage();
        String rootDirectory = new File("").getAbsolutePath();
        path = Paths.get(rootDirectory + "\\src\\main\\resources\\static\\product-images\\" + createdProduct.getId() + ".png");

        if (productImage != null && !productImage.isEmpty()) {
            try {
                productImage.transferTo(new File(path.toString()));
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Product image saving failed", e);
            }
        }

        return "redirect:/admin/productInventory";
    }

    @GetMapping("/productInventory/deleteProduct/{uuid}")
    public String deleteProductPage(@PathVariable UUID uuid, HttpServletRequest request) throws NotFoundException {
        log.info("Deleting id is : {}", productService.getByUUID(uuid).toString());
        List<CustomerOrderDto> customerOrderDtos = customerOrderService.getAll();

        ProductDto productDto = productService.getByUUID(uuid);

        customerOrderDtos.forEach(customerOrderDto -> {
            if (customerOrderDto.getProduct().getUuid().equals(uuid)) {
                try {
                    customerOrderService.updateCustomerByProduct(customerOrderDto);
                    customerOrderService.delete(customerOrderDto.getUuid());
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        String rootDirectory = new File("").getAbsolutePath();
        path = Paths.get(rootDirectory + "\\src\\main\\resources\\static\\product-images\\" + productDto.getId() + ".png");

        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        productService.delete(uuid);
        return "redirect:/admin/productInventory";
    }

    @GetMapping("/productInventory/editProduct/{uuid}")
    public String editProductGet(@PathVariable UUID uuid, Model model) throws NotFoundException {
        ProductDto productDto = productService.getByUUID(uuid);
        model.addAttribute("product", productDto);
        return "editProduct";
    }

    @PostMapping("/productInventory/editProduct/{uuid}")
    public String editProductPost(@Valid @PathVariable UUID uuid,
                                  @ModelAttribute("product") ProductDto productDto, Model model, HttpServletRequest request, BindingResult result) throws NotFoundException, IOException {

        if (result.hasErrors())
            return "editProduct";

        ProductDto updatedProduct = productService.getByUUID(uuid);

        MultipartFile productImage = productDto.getProductImage();
        String rootDirectory = new File("").getAbsolutePath();
        path = Paths.get(rootDirectory + "\\src\\main\\resources\\static\\product-images\\" + updatedProduct.getId() + ".png");

        if (productImage != null && !productImage.isEmpty()) {
            try {
                productImage.transferTo(new File(path.toString()));
            } catch (Exception e) {
                throw new RuntimeException("Product image saving failed", e);
            }
        }
        productService.update(productDto);
        return "redirect:/admin/productInventory";
    }

    @GetMapping("/customers")
    public String customerList(Model model) {
        List<CustomerDto> customerDtos = customerService.getAll();
        model.addAttribute("customers", customerDtos);
        return "customerManagement";
    }

    @GetMapping("/customers/edit/{customerUuid}")
    public String editCustomerGet(@PathVariable UUID customerUuid, Model model) throws NotFoundException {
        CustomerDto customerDto = customerService.getByUUID(customerUuid);
        model.addAttribute("customer", customerDto);
        return "editCustomer";
    }

    @PostMapping("/customers/edit/{customerUuid}")
    public String editCustomer(@Valid @ModelAttribute("customer") CustomerDto customer,
                               @PathVariable UUID customerUuid, BindingResult bindingResult, Model model) throws NotFoundException {

        CustomerDto updatedCustomer = customerService.getByUUID(customerUuid);
        boolean check = false;
        if (bindingResult.hasErrors()) {
            return "editCustomer";
        }
        List<CustomerDto> customerDtos = customerService.getAll();

        for (CustomerDto customerDto : customerDtos) {

            if (customerDto.getCustomerEmail().equals(customer.getCustomerEmail())) {
                if (!customerDto.getUuid().equals(updatedCustomer.getUuid())) {
                    model.addAttribute("emailDup", "Email already exist");
                    check = true;
                }
            }
            if (customerDto.getCustomerUsername().equals(customer.getCustomerUsername())) {
                if (!customerDto.getUuid().equals(updatedCustomer.getUuid())) {
                    model.addAttribute("usernameDup", "Username already exist");
                    check = true;
                }
            }
            if (check) {
                return "editCustomer";
            }
        }

        customer.setCustomerPassword(passwordEncoder.encode(customer.getCustomerPassword()));

        UserDto user = updatedCustomer.getUser();
        user.setUsername(customer.getCustomerUsername());
        user.setPassword(customer.getCustomerPassword());
        user.setEnabled(customer.isEnabled());
        userService.update(user);

        customer.setUuid(updatedCustomer.getUuid());
        customer.setUser(user);
        customer.setCart(updatedCustomer.getCart());

        customerService.update(customer);
        return "redirect:/admin/customers";

    }

    @GetMapping("/customers/delete/{uuid}")
    public String deleteCustomer(@PathVariable UUID uuid) throws NotFoundException {
        List<CustomerOrderDto> customerOrderDtos = customerOrderService.getAll();
        for (CustomerOrderDto customerOrderDto : customerOrderDtos) {
            if (customerOrderDto.getCustomer().getUuid().equals(uuid)) {
                customerOrderService.delete(customerOrderDto.getUuid());
            }
        }
        customerService.delete(uuid);
        return "redirect:/admin/customers";
    }

    @GetMapping("/orders")
    public String orderList(Model model) {
        List<CustomerOrderDto> customerOrderDtos = customerOrderService.getAll();
        model.addAttribute("orders", customerOrderDtos);
        return "orderManagement";
    }

    @GetMapping("/orders/delete/{uuid}")
    public String deleteOrder(@PathVariable UUID uuid) throws NotFoundException {
        customerOrderService.delete(uuid);
        return "redirect:/admin/orders";
    }

}
