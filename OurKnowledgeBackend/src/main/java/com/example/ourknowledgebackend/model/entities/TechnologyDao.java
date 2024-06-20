package com.example.ourknowledgebackend.model.entities;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface TechnologyDao extends CrudRepository<Technology, Long> {

    boolean existsByNameAndParentId(String name, Long parentId);

    List<Technology> findAllByParentId(Long parentId);

    List<Technology> findAllByRelevantTrue();


    Optional<Technology> findByNameAndParentId(String name, Long parentId);
}
