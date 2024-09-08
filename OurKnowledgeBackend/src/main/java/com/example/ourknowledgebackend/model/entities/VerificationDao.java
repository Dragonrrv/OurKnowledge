package com.example.ourknowledgebackend.model.entities;

import com.example.ourknowledgebackend.model.UsesTechnology;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface VerificationDao extends CrudRepository<Verification, Long> {

    List<Verification> findAllByUsesId(Long usesId);

    List<Verification> findAllByKnowledgeId(Long knowledgeId);

    List<Verification> findAllByKnowledgeUser(User user);

    List<Verification> findAllByUsesProject(Project project);

    List<Verification> findAllByKnowledgeUserAndUsesProject(User user, Project project);

    boolean existsByKnowledgeUserAndKnowledgeTechnologyId(User user, Long technologyId);

    Optional<Verification> findByUsesProjectAndKnowledgeUserAndKnowledgeTechnologyId(Project project, User user, Long parentId);

    List<Verification> findAllByUsesProjectAndKnowledgeUserAndKnowledgeTechnologyParentId(Project project, User user, Long parentId);
}
