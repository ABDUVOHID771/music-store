package com.example.musicstore.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CartDto extends BaseDto {

    private List<CartItemDto> cartItems;

    private double grandTotal;

}
