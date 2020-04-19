package com.example.musicstore;

import com.example.musicstore.dao.domain.*;
import com.example.musicstore.mapping.CustomerMapper;
import com.example.musicstore.mapping.ProductMapper;
import com.example.musicstore.mapping.UserMapper;
import com.example.musicstore.service.CustomerService;
import com.example.musicstore.service.ProductService;
import com.example.musicstore.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@Component
public class DataLoader implements CommandLineRunner {

    private final ProductService productService;
    private final ProductMapper productMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(CustomerMapper customerMapper, CustomerService customerService, ProductService productService, ProductMapper productMapper, UserService userService, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.productService = productService;
        this.customerMapper = customerMapper;
        this.customerService = customerService;
        this.productMapper = productMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        try {
            loader();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loader() {

        String rootDirectory = new File("").getAbsolutePath();
        Path path = Paths.get(rootDirectory + "\\src\\main\\resources\\static\\product-images\\");
        Set<String> files = Stream.of(Objects.requireNonNull(new File(path.toString()).listFiles()))
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
        files.forEach(file ->
        {
            int integer = Integer.parseInt(file.replaceAll(".png", ""));
            if (integer > 20) {
                try {
                    Files.delete(Paths.get(path.toString() + "\\" + file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        IntStream.range(0, 20).forEach(i -> {
            Product product = new Product();
            product.setProductName("Guitar " + i);
            product.setProductCategory("Instrument " + i);
            product.setProductDescription("This is a fender strat guitar " + i);
            product.setProductPrice(1200 + i);
            product.setProductCondition("new");
            product.setProductStatus("Active");
            product.setUnitInStock(11 + i);
            product.setProductManufactures("Fender");
            productService.create(productMapper.entityToDto(product));
        });

        User user = new User();
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setEnabled(true);
        user.setAuthorities("ADMIN");
        userService.create(userMapper.entityToDto(user));

        Address address = new Address();
        address.setZipCode("123");
        address.setStreetName("UZ");
        address.setState("UZ");
        address.setCountry("Uz");
        address.setCity("UZ");
        address.setApartmentNumber("123");

        Customer customer = new Customer();
        log.info("Customer uuid : {}", customer.getUuid());
        log.info("Customer id : {}", customer.getId());
        customer.setEnabled(true);
        customer.setCustomerUsername("user");
        customer.setCustomerPassword(passwordEncoder.encode("user"));
        customer.setCustomerEmail("usermail@gmail.com");
        customer.setCustomerName("user");
        customer.setCustomerPhone("1234567");
        customer.setAddress(address);
        User user1 = new User();
        user1.setUsername(customer.getCustomerUsername());
        user1.setPassword(customer.getCustomerPassword());
        user1.setAuthorities("USER");
        user1.setEnabled(true);
        customer.setUser(user1);
        customer.setCart(new Cart());
        customerService.create(customerMapper.entityToDto(customer));
    }
}