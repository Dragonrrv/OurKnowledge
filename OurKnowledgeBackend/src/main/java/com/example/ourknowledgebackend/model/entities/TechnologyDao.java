package com.example.ourknowledgebackend.model.entities;

import java.util.List;
import java.util.Optional;

import com.example.ourknowledgebackend.model.KnownTechnology;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TechnologyDao extends CrudRepository<Technology, Long> {

    boolean existsByNameAndParentId(String name, Long parentId);

    List<Technology> findAllByParentId(Long parentId);

    List<Technology> findAllByRelevantTrue();

    Optional<Technology> findByNameAndParentId(String name, Long parentId);

    @Query("SELECT new com.example.ourknowledgebackend.model.KnownTechnology(t.id, t.name, t.parentId, t.relevant, k.id, k.mainSkill, k.likeIt) " +
            "FROM Technology t " +
            "LEFT JOIN Knowledge k ON t.id = k.technology.id AND k.user.id = :userId " +
            "WHERE t.relevant = true")
    List<KnownTechnology> findRelevantTechnologiesWithKnowledge(@Param("userId") Long userId);
}
