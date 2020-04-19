package com.example.musicstore.mapping;

import com.example.musicstore.dao.domain.Product;
import com.example.musicstore.dto.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper extends BaseMapper<ProductDto, Product> {

    Product updateProductFromDto(ProductDto productDto, @MappingTarget Product product);

}
