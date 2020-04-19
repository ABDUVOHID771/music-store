package com.example.musicstore.dao.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "customer")
public class Customer extends BaseEntity {

    @Column(name = "customer_name")
    @NotEmpty(message = "The customer name must not be null")
    private String customerName;

    @Column(name = "customer_email")
    @NotEmpty(message = "The customer email must not be null")
    @Email
    private String customerEmail;

    private String customerPhone;

    @Column(name = "customer_username")
    @NotEmpty(message = "The customer username must not be null")
    private String customerUsername;

    @Column(name = "customer_password")
    @NotEmpty(message = "The customer password must not be null")
    private String customerPassword;

    private boolean enabled;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

}
