package com.example.musicstore.service;

import com.example.musicstore.dao.domain.User;
import com.example.musicstore.dao.repository.UserRepository;
import com.example.musicstore.dto.UserDto;
import com.example.musicstore.mapping.UserMapper;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService extends BaseService<UserRepository, UserMapper, User, UserDto> {

    private final UserRepository userRepository;

    public UserService(UserRepository repository, UserMapper mapper) {
        super(repository, mapper);
        this.userRepository = repository;
    }

    public User findByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User does not exist !");
        }
        return user;
    }

    @Override
    public UserDto update(UserDto input) throws NotFoundException {
        User user = getRepository().findByUuid(input.getUuid()).orElseThrow(() -> new NotFoundException("Not Found"));
        User updatedUser = getMapper().updateUserFromDto(input, user);
        User saveUpdated = getRepository().save(updatedUser);
        return getMapper().entityToDto(saveUpdated);
    }
}
