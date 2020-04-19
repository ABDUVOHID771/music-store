package com.example.musicstore.controller;

import com.example.musicstore.dto.*;
import com.example.musicstore.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
public class RegisterController {

    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;

    public RegisterController(CustomerService customerService, PasswordEncoder passwordEncoder) {
        this.customerService = customerService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String registerCustomer(Model model) {
        model.addAttribute("customer", new CustomerDto());
        return "registerCustomer";
    }

    @PostMapping("/register")
    public String registerCustomerPost(@Valid @ModelAttribute("customer") CustomerDto customer,
                                       BindingResult bindingResult, Model model) {

        boolean check = false;
        if (bindingResult.hasErrors()) {
            return "registerCustomer";
        }

        List<CustomerDto> customerDtos = customerService.getAll();

        for (CustomerDto customerDto : customerDtos) {

            if (customerDto.getCustomerEmail().equals(customer.getCustomerEmail())) {
                model.addAttribute("emailDup", "Email already exist");
                check = true;
            }
            if (customerDto.getCustomerUsername().equals(customer.getCustomerUsername())) {
                model.addAttribute("usernameDup", "Username already exist");
                check = true;
            }
            if (check) {
                return "registerCustomer";
            }
        }

        customer.setEnabled(true);
        customer.setCustomerPassword(passwordEncoder.encode(customer.getCustomerPassword()));

        UserDto userDto = new UserDto();
        userDto.setUsername(customer.getCustomerUsername());
        userDto.setPassword(customer.getCustomerPassword());
        userDto.setAuthorities("USER");
        userDto.setEnabled(true);
        customer.setUser(userDto);

        CartDto cartDto = new CartDto();
        customer.setCart(cartDto);

        CustomerDto savedCustomer = customerService.create(customer);

        log.info("Saved customer data is : {}", savedCustomer);

        model.addAttribute("customer", savedCustomer);
        return "registerCustomerSuccess";
    }

}
