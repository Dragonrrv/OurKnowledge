package com.example.ourknowledgebackend.model.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UsesDao extends CrudRepository<Uses, Long> {
    void deleteAllByProjectId(Long projectId);

    List<Uses> findAllByProject(Project project);

    Uses findByProjectAndTechnologyId(Project project, Long technologyId);
}
