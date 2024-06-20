package com.example.ourknowledgebackend.model.entities;

import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<User, Long> {

    User findByNameAndPassword(String name, String password);
}
