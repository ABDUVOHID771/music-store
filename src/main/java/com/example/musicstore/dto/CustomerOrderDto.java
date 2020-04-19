package com.example.musicstore.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerOrderDto extends BaseDto {

    @ToString.Exclude
    private ProductDto product;

    @ToString.Exclude
    private CustomerDto customer;

    private Instant createdDate;

    private int quantity;

    private double totalPrice;

    private double cartGrandTotal;
}

