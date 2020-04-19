package com.example.musicstore.service;

import com.example.musicstore.dao.domain.Cart;
import com.example.musicstore.dao.repository.CartRepository;
import com.example.musicstore.dto.CartDto;
import com.example.musicstore.mapping.CartMapper;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CartService extends BaseService<CartRepository, CartMapper, Cart, CartDto> {

    public CartService(CartRepository repository, CartMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public CartDto update(CartDto input) throws NotFoundException {
        Cart cart = getRepository().findByUuid(input.getUuid()).orElseThrow(() -> new NotFoundException("Not Found"));
        Cart updated = getMapper().updateCartFromDto(input, cart);
        Cart saveUpdated = getRepository().save(updated);
        return getMapper().entityToDto(saveUpdated);
    }

}
