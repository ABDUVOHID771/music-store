package com.example.musicstore.mapping;

import com.example.musicstore.dao.domain.CustomerOrder;
import com.example.musicstore.dto.CustomerOrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerOrderMapper extends BaseMapper<CustomerOrderDto, CustomerOrder> {

    CustomerOrder updateCustomerOrder(CustomerOrderDto customerOrderDto, @MappingTarget CustomerOrder customerOrder);
}
