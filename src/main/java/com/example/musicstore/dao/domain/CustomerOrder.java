package com.example.musicstore.dao.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "customer_order")
@EntityListeners(AuditingEntityListener.class)
public class CustomerOrder extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(nullable = false)
    @CreatedDate
    private Instant createdDate;

    private int quantity;
    private double totalPrice;
    private double cartGrandTotal;

}
