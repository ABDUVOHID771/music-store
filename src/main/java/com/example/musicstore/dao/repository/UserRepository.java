package com.example.musicstore.dao.repository;

import com.example.musicstore.dao.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User> {
    User findByUsername(String username);
}
