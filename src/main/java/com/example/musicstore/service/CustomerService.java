package com.example.musicstore.service;

import com.example.musicstore.dao.domain.Cart;
import com.example.musicstore.dao.domain.Customer;
import com.example.musicstore.dao.repository.CustomerRepository;
import com.example.musicstore.dto.*;
import com.example.musicstore.mapping.CartMapper;
import com.example.musicstore.mapping.CustomerMapper;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService extends BaseService<CustomerRepository, CustomerMapper, Customer, CustomerDto> {

    private final CartMapper cartMapper;

    public CustomerService(CustomerRepository repository, CustomerMapper mapper, CartMapper cartMapper) {
        super(repository, mapper);
        this.cartMapper = cartMapper;
    }

    @Override
    public CustomerDto update(CustomerDto input) throws NotFoundException {
        Customer customer = getRepository().findByUuid(input.getUuid()).orElseThrow(() -> new NotFoundException("Not Found"));
        Customer updatedCustomer = getMapper().updateCustomerFromDto(input, customer);
        Customer saveUpdated = getRepository().save(updatedCustomer);
        return getMapper().entityToDto(saveUpdated);
    }

    public CustomerDto getByUsername(String username) {
        Customer customer = getRepository().findCustomerByCustomerUsername(username);
        return getMapper().entityToDto(customer);
    }

    public CustomerDto findByCart(CartDto cartDto) {
        Cart cart = cartMapper.dtoToEntity(cartDto);
        Customer customer = getRepository().findCustomerByCart(cart);
        return getMapper().entityToDto(customer);
    }

}
