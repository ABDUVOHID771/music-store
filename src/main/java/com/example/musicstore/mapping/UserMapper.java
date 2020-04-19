package com.example.musicstore.mapping;

import com.example.musicstore.dao.domain.User;
import com.example.musicstore.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper extends BaseMapper<UserDto, User> {

    User updateUserFromDto(UserDto userDto, @MappingTarget User user);
}
