package com.example.musicstore.service;

import com.example.musicstore.dao.domain.BaseEntity;
import com.example.musicstore.dao.repository.BaseRepository;
import com.example.musicstore.dto.BaseDto;
import com.example.musicstore.mapping.BaseMapper;
import javassist.NotFoundException;
import lombok.Getter;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public abstract class BaseService<R extends BaseRepository<E>, M extends BaseMapper<D, E>, E extends BaseEntity, D extends BaseDto> {

    private R repository;
    private M mapper;

    public BaseService(R repository, M mapper) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public D create(D input) {
        E entity = mapper.dtoToEntity(input);
        E savedEntity = repository.save(entity);
        return mapper.entityToDto(savedEntity);
    }

    public D getByUUID(UUID uuid) throws NotFoundException {
        Optional<E> entity = repository.findByUuid(uuid);
        if (entity.isEmpty()) {
            throw new NotFoundException("Not Found");
        }
        return mapper.entityToDto(entity.get());
    }

    public List<D> getAll() {
        return repository.findAll().stream()
                .map(entity -> mapper.entityToDto(entity))
                .collect(Collectors.toList());
    }

    public abstract D update(D input) throws NotFoundException;

    @Transactional
    public void delete(UUID uuid) throws NotFoundException {
        Optional<E> entity = repository.findByUuid(uuid);
        if (entity.isEmpty())
            throw new NotFoundException("Not Found");
        repository.deleteByUuid(uuid);
    }

}
