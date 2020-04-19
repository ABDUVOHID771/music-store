package com.example.musicstore.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerDto extends BaseDto {

    @NotEmpty(message = "The customer name must not be null")
    private String customerName;
    @NotEmpty(message = "The customer email must not be null")
    private String customerEmail;
    private String customerPhone;

    @NotEmpty(message = "The customer username must not be null")
    private String customerUsername;

    @NotEmpty(message = "The customer password must not be null")
    private String customerPassword;
    private boolean enabled;

    private AddressDto address;
    private UserDto user;
    private CartDto cart;
}
