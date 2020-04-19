package com.example.musicstore.service;

import com.example.musicstore.dao.domain.Address;
import com.example.musicstore.dao.repository.AddressRepository;
import com.example.musicstore.dto.AddressDto;
import com.example.musicstore.mapping.AddressMapper;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AddressService extends BaseService<AddressRepository, AddressMapper, Address, AddressDto> {

    public AddressService(AddressRepository repository, AddressMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public AddressDto update(AddressDto input) throws NotFoundException {
        Address address = getRepository().findByUuid(input.getUuid())
                .orElseThrow(() -> new NotFoundException("Not Found"));
        if (input.getApartmentNumber() != null) {
            address.setApartmentNumber(input.getApartmentNumber());
        }
        if (input.getCity() != null) {
            address.setCity(input.getCity());
        }
        if (input.getCountry() != null) {
            address.setCountry(input.getCountry());
        }
        if (input.getState() != null) {
            address.setState(input.getState());
        }
        if (input.getStreetName() != null) {
            address.setStreetName(input.getStreetName());
        }
        if (input.getZipCode() != null) {
            address.setZipCode(input.getZipCode());
        }
        Address updatedEntity = getRepository().save(address);
        return getMapper().entityToDto(updatedEntity);
    }
}
