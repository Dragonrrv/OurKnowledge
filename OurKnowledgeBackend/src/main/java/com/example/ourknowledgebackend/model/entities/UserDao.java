package com.example.ourknowledgebackend.model.entities;

import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<User, Long> , CustomizedUserDao {

    boolean existsByEmail(String email);

    User findByEmail(String email);
}
