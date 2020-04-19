package com.example.musicstore.mapping;

import com.example.musicstore.dao.domain.Cart;
import com.example.musicstore.dto.CartDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CartMapper extends BaseMapper<CartDto, Cart> {

    Cart updateCartFromDto(CartDto cartDto, @MappingTarget Cart cart);
}