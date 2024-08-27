package com.example.ourknowledgebackend.model.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface VerificationDao extends CrudRepository<Verification, Long> {

    List<Verification> findAllByProject(Project project);

    List<Verification> findAllByProjectAndKnowledge_Technology_Id(Project project, Long technologyId);

    boolean existsByProjectAndKnowledge(Project project, Knowledge knowledge);

    List<Verification> findAllByProjectAndKnowledge_UserAndKnowledge_Technology_ParentId(Project project, User user, Long parentId);

    Optional<Verification> findByProjectAndKnowledge(Project project, Knowledge knowledge);

    boolean existsByKnowledge_UserAndKnowledge_Technology_Id(User user, Long technologyId);
}
