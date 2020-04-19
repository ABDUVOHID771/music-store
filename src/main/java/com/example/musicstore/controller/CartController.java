package com.example.musicstore.controller;

import com.example.musicstore.dao.domain.UserPrincipal;
import com.example.musicstore.dto.CustomerDto;
import com.example.musicstore.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;


@Slf4j
@Controller
@RequestMapping("/customer/cart")
public class CartController {

    private final CustomerService customerService;

    public CartController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping({"", "/"})
    public String getCart(@AuthenticationPrincipal UserPrincipal activeUser) {
        CustomerDto customerDto = customerService.getByUsername(activeUser.getUsername());
        UUID cartUUID = customerDto.getCart().getUuid();
        return "redirect:/customer/cart/" + cartUUID;
    }

    @GetMapping("/{cartUUID}")
    public String getCart(@PathVariable UUID cartUUID, Model model) {
        model.addAttribute("cartId", cartUUID);
        return "cart";
    }
}
