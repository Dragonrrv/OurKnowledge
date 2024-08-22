package com.example.ourknowledgebackend.model.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VerificationDao extends CrudRepository<Verification, Long> {

    List<Verification> findAllByProject(Project project);

    List<Verification> findAllByProjectAndKnowledge_Technology_Id(Project project, Long technologyId);
}
