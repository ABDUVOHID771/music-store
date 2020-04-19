package com.example.musicstore.dao.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product")
public class Product extends BaseEntity {

    @Column(name = "product_name")
    @NotEmpty(message = "The product name must not be null")
    private String productName;

    @Column(name = "product_price")
    @Min(value = 0, message = "The product price must not be less than zero")
    private double productPrice;

    @Column(name = "unit_in_stock")
    @Min(value = 0, message = "The product unit must not be less than zero ")
    private int unitInStock;

    @Transient
    private MultipartFile productImage;

    @Column(name = "product_category")
    private String productCategory;

    @Column(name = "product_description")
    private String productDescription;

    @Column(name = "product_condition")
    private String productCondition;

    @Column(name = "product_status")
    private String productStatus;

    @Column(name = "product_manufactures")
    private String productManufactures;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference
    private List<CartItem> cartItems;

}
