package com.example.ourknowledgebackend.model.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProjectDao extends CrudRepository<Project, Long> {
    Optional<Project> findByName(String name);

    boolean existsByName(String name);
}
