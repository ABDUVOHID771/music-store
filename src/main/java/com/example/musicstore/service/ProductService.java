package com.example.musicstore.service;

import com.example.musicstore.dao.domain.CartItem;
import com.example.musicstore.dao.domain.Product;
import com.example.musicstore.dao.repository.ProductRepository;
import com.example.musicstore.dto.CartItemDto;
import com.example.musicstore.dto.ProductDto;
import com.example.musicstore.mapping.CartItemMapper;
import com.example.musicstore.mapping.ProductMapper;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ProductService extends BaseService<ProductRepository, ProductMapper, Product, ProductDto> {

    public ProductService(ProductRepository repository, ProductMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public ProductDto update(ProductDto input) throws NotFoundException {
        Product product = getRepository().findByUuid(input.getUuid()).orElseThrow(() -> new NotFoundException("Not Found"));
        Product updated = getMapper().updateProductFromDto(input, product);
        Product saveUpdated = getRepository().save(updated);
        return getMapper().entityToDto(saveUpdated);
    }
}
