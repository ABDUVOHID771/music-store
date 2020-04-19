package com.example.musicstore.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Lob;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductDto extends BaseDto {

    private List<CartItemDto> cartItems;

    @NotEmpty(message = "The product name must not be null")
    private String productName;
    private String productCategory;
    private String productDescription;
    private String productCondition;
    private String productStatus;
    private String productManufactures;

    @Min(value = 0, message = "The product price must not be less than zero")
    private double productPrice;

    @Min(value = 0, message = "The product unit must not be less than zero ")
    private int unitInStock;

    @Transient
    private MultipartFile productImage;
}
