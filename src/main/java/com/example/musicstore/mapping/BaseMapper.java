package com.example.musicstore.mapping;

import com.example.musicstore.dao.domain.BaseEntity;
import com.example.musicstore.dto.BaseDto;

public interface BaseMapper<D extends BaseDto, E extends BaseEntity> {

    D entityToDto(E entity);

    E dtoToEntity(D dto);
}
