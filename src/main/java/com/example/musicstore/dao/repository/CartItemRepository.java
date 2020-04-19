package com.example.musicstore.dao.repository;

import com.example.musicstore.dao.domain.CartItem;
import org.springframework.stereotype.Repository;


@Repository
public interface CartItemRepository extends BaseRepository<CartItem> {
    CartItem getCartItemByProduct_Id(Long productId);
}
