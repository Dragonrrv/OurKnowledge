package com.example.ourknowledgebackend.model.entities;

import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProjectDao extends CrudRepository<Project, Long>, CustomizedProjectDao {

    Optional<Project> findByName(String name);

    Optional<Project> findByNameAndIdIsNot(String name, Long id);

    boolean existsByName(String name);

    boolean existsByNameAndIdIsNot(String name, Long id);
}
