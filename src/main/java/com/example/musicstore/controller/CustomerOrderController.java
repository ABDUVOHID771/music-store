package com.example.musicstore.controller;

import com.example.musicstore.dto.*;
import com.example.musicstore.service.*;
import javassist.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
public class CustomerOrderController {

    private final CartService cartService;
    private final CustomerOrderService customerOrderService;
    private final CustomerService customerService;
    private final CartItemService cartItemService;

    public CustomerOrderController(CustomerOrderService customerOrderService,
                                   CartService cartService,
                                   CustomerService customerService,
                                   CartItemService cartItemService) {
        this.customerOrderService = customerOrderService;
        this.cartService = cartService;
        this.customerService = customerService;
        this.cartItemService = cartItemService;
    }

    @GetMapping("/order/{cartUuid}")
    public String createOrder(@PathVariable UUID cartUuid, Model model) throws NotFoundException {

        CartDto cartDto = cartService.getByUUID(cartUuid);
        CustomerDto customerDto = customerService.findByCart(cartDto);

        model.addAttribute("customer", customerDto);
        return "customerOrder/orderConfirmation";
    }

    @GetMapping("/order/thank/{cartUuid}")
    public String thankYou(@PathVariable UUID cartUuid) throws NotFoundException {

        boolean check = false;

        CartDto cartDto = cartService.getByUUID(cartUuid);
        CustomerDto customerDto = customerService.findByCart(cartDto);
        AddressDto addressDto = customerDto.getAddress();

        if (addressDto == null || addressDto.getApartmentNumber().isEmpty() || addressDto.getCity().isEmpty()
                || addressDto.getCountry().isEmpty() || addressDto.getState().isEmpty()
                || addressDto.getStreetName().isEmpty() || addressDto.getZipCode().isEmpty()) {
            check = true;
        }
        if (cartDto.getCartItems().isEmpty() || check) {
            return "customerOrder/invalidCartWarning";
        }

        cartItemService.addCustomerOrder(cartDto, customerDto);

        cartItemService.removeAllCartItems(cartDto);

        return "customerOrder/thankCustomer";
    }

}
