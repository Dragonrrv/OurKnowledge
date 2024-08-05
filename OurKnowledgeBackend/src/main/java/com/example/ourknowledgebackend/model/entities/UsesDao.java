package com.example.ourknowledgebackend.model.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UsesDao extends CrudRepository<Uses, Long> {
    void deleteByProjectId(Long projectId);

    List<Uses> findAllByProject(Project project);
}
