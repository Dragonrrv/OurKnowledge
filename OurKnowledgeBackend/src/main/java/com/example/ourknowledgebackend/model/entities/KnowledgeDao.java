package com.example.ourknowledgebackend.model.entities;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface KnowledgeDao extends CrudRepository<Knowledge, Long> {

    List<Knowledge> findAllByUser(User user);

    List<Knowledge> findAllByUserAndTechnologyParentId(User user, Long parentId);

    boolean existsByTechnology(Technology technology);

    List<Knowledge> findAllByTechnology(Technology technology);

    boolean existsByUserAndTechnology(User user, Technology technology);

    Optional<Knowledge> findByUserAndTechnology(User user, Technology technology);

    Knowledge findByUserAndTechnologyId(User user, Long parentId);
}
