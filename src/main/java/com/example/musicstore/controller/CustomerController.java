package com.example.musicstore.controller;

import com.example.musicstore.dao.domain.UserPrincipal;
import com.example.musicstore.dto.CustomerDto;
import com.example.musicstore.dto.UserDto;
import com.example.musicstore.service.CustomerService;
import com.example.musicstore.service.UserService;
import javassist.NotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Controller
public class CustomerController {

    private final CustomerService customerService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public CustomerController(CustomerService customerService, UserService userService, PasswordEncoder passwordEncoder) {
        this.customerService = customerService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/customer")
    public String customerInfoPage(@AuthenticationPrincipal UserPrincipal activeUser, Model model) {
        CustomerDto customerDto = customerService.getByUsername(activeUser.getUsername());
        model.addAttribute("customer", customerDto);
        return "customerInformation";
    }

    @GetMapping("/customer/edit/{customerUuid}")
    public String editCustomerInfo(@PathVariable UUID customerUuid, Model model) throws NotFoundException {
        CustomerDto customerDto = customerService.getByUUID(customerUuid);
        model.addAttribute("customer", customerDto);
        return "customerEditView";
    }

    @PostMapping("/customer/edit/{customerUuid}")
    public String updateCustomer(@Valid @PathVariable UUID customerUuid,
                                 @ModelAttribute("customer") CustomerDto customer,
                                 BindingResult bindingResult, Model model) throws NotFoundException {

        CustomerDto updatedCustomer = customerService.getByUUID(customerUuid);
        boolean check = false;
        if (bindingResult.hasErrors()) {
            return "customerEditView";
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
                return "customerEditView";
            }
        }

        customer.setCustomerPassword(passwordEncoder.encode(customer.getCustomerPassword()));

        UserDto user = updatedCustomer.getUser();
        user.setUsername(customer.getCustomerUsername());
        user.setPassword(customer.getCustomerPassword());
        userService.update(user);

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userPrincipal.setUsername(user.getUsername());

        customer.setUuid(updatedCustomer.getUuid());
        customer.setUser(user);
        customer.setCart(updatedCustomer.getCart());
        customer.setEnabled(updatedCustomer.isEnabled());

        System.out.println("Updated customer details : " + customer.toString());

        customerService.update(customer);
        return "redirect:/customer";
    }

}
