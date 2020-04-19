package com.example.musicstore.mapping;

import com.example.musicstore.dao.domain.CartItem;
import com.example.musicstore.dto.CartItemDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = {CartMapper.class, ProductMapper.class},
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CartItemMapper extends BaseMapper<CartItemDto, CartItem> {

    CartItem cartItemUpdateFromCartItemDto(CartItemDto cartItemDto, @MappingTarget CartItem cartItem);
}