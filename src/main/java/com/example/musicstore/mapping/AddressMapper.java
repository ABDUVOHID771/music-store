package com.example.musicstore.mapping;

import com.example.musicstore.dao.domain.Address;
import com.example.musicstore.dto.AddressDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper extends BaseMapper<AddressDto, Address> {
}
