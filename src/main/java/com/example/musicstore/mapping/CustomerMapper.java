package com.example.musicstore.mapping;

import com.example.musicstore.dao.domain.Customer;
import com.example.musicstore.dto.CustomerDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper extends BaseMapper<CustomerDto, Customer> {

    Customer updateCustomerFromDto(CustomerDto customerDto, @MappingTarget Customer customer);
}
