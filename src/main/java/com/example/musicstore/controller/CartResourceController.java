package com.example.musicstore.controller;

import com.example.musicstore.dao.domain.UserPrincipal;
import com.example.musicstore.dto.CartDto;
import com.example.musicstore.dto.CartItemDto;
import com.example.musicstore.dto.CustomerDto;
import com.example.musicstore.dto.ProductDto;
import com.example.musicstore.service.CartItemService;
import com.example.musicstore.service.CartService;
import com.example.musicstore.service.CustomerService;
import com.example.musicstore.service.ProductService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@Slf4j
@RestController
public class CartResourceController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final CustomerService customerService;
    private final ProductService productService;

    public CartResourceController(CartService cartService, CartItemService cartItemService,
                                  CustomerService customerService, ProductService productService) {
        this.cartItemService = cartItemService;
        this.cartService = cartService;
        this.customerService = customerService;
        this.productService = productService;
    }

    @RequestMapping("/rest/cart/{cartUUID}")
    public @ResponseBody
    CartDto getCartByUUID(@PathVariable(value = "cartUUID") UUID uuid) throws NotFoundException {
        return cartService.getByUUID(uuid);
    }

    @GetMapping("/rest/cart/add/{productUUID}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void addItem(@PathVariable UUID productUUID, @AuthenticationPrincipal UserPrincipal activeUser) throws NotFoundException {

        CustomerDto customerDto = customerService.getByUsername(activeUser.getUsername());
        CartDto cartDto = customerDto.getCart();
        ProductDto productDto = productService.getByUUID(productUUID);
        List<CartItemDto> cartItems = cartDto.getCartItems();

        for (CartItemDto cartItem : cartItems) {
            if (productDto.getId().equals(cartItem.getProduct().getId())) {

                cartItem.setQuantity(cartItem.getQuantity() + 1);
                cartItem.setTotalPrice(productDto.getProductPrice() * cartItem.getQuantity());
                cartItemService.create(cartItem);
                cartDto.setGrandTotal(cartItemService.getGrandTotalForCart(cartDto));
                cartService.update(cartDto);
                return;
            }
        }

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setQuantity(1);
        cartItemDto.setTotalPrice(productDto.getProductPrice() * cartItemDto.getQuantity());
        cartItemService.setCartForProduct(cartItemDto, cartDto, productDto);

    }

    @RequestMapping(value = "/rest/cart/remove/{productUUID}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void removeItem(@PathVariable(value = "productUUID") UUID productUUID) throws NotFoundException {
        CartItemDto cartItemDto = cartItemService.getByProduct(productService.getByUUID(productUUID));
        log.info("Deleting cart item is {} ", cartItemDto);
        cartItemService.removeCartItem(cartItemDto);
    }

    @RequestMapping(value = "/rest/cart/clear/{cartUUID}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void clearCart(@PathVariable(value = "cartUUID") UUID cartUUID) throws NotFoundException {
        log.info("Deleting cart items from : {}", cartUUID);
        CartDto cartDto = cartService.getByUUID(cartUUID);
        cartItemService.removeAllCartItems(cartDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Illegal request, please verify your payload.")
    public void handleClientErrors(Exception e) {
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Internal Server Error.")
    public void handleServerErrors(Exception e) {
    }
}
