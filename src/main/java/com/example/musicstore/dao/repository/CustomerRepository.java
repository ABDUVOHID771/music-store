package com.example.musicstore.dao.repository;

import com.example.musicstore.dao.domain.Cart;
import com.example.musicstore.dao.domain.Customer;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends BaseRepository<Customer> {

    Customer findCustomerByCustomerUsername(String username);

    Customer findCustomerByCart(Cart cart);
}
