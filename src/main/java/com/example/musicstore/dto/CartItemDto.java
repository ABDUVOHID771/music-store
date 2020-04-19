package com.example.musicstore.dto;

import com.example.musicstore.dao.domain.Cart;
import com.example.musicstore.dao.domain.Product;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
public class CartItemDto extends BaseDto {

    @ToString.Exclude
    private Product product;
    @ToString.Exclude
    private Cart cart;

    private int quantity;
    private double totalPrice;

}