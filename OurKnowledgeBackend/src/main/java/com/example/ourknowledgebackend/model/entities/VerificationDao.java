package com.example.ourknowledgebackend.model.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface VerificationDao extends CrudRepository<Verification, Long> {

    List<Verification> findAllByUsesProjectAndKnowledgeUserAndKnowledgeTechnologyParentId(Project project, User user, Long parentId);

    boolean existsByKnowledgeUserAndKnowledgeTechnologyId(User user, Long technologyId);


    Optional<Verification> findByKnowledgeUserAndUses(User user, Uses uses);

    Optional<Verification> findByKnowledgeUserAndKnowledgeTechnologyId(User user, Long parentId);

    List<Verification> findAllByKnowledgeUser(User user);
}
