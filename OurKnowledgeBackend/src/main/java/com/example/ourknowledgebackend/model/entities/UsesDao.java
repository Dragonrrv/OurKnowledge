package com.example.ourknowledgebackend.model.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UsesDao extends CrudRepository<Uses, Long> {
     boolean existsByProjectAndTechnology(Project project, Technology technology);

    List<Uses> findAllByProject(Project project);

    Uses findByProjectAndTechnologyId(Project project, Long technologyId);

    List<Uses> findAllByProjectAndTechnologyParentId(Project project, Long parentId);

    Optional<Uses> findByProjectAndTechnology(Project project, Technology technology);
}
