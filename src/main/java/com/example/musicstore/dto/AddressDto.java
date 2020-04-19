package com.example.musicstore.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class AddressDto extends BaseDto {

    @NotEmpty
    private String streetName;
    @NotEmpty
    private String apartmentNumber;
    @NotEmpty
    private String city;
    @NotEmpty
    private String state;
    @NotEmpty
    private String country;
    @NotEmpty
    private String zipCode;
}

