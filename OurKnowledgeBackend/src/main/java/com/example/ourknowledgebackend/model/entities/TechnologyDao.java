package com.example.ourknowledgebackend.model.entities;

import java.util.List;
import java.util.Optional;

import com.example.ourknowledgebackend.model.KnownTechnology;
import com.example.ourknowledgebackend.model.UsesTechnology;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TechnologyDao extends CrudRepository<Technology, Long> {

    List<Technology> findAllByParentId(Long parentId);

    List<Technology> findAllByRelevantTrue();

    Optional<Technology> findByNameAndParentId(String name, Long parentId);

    @Query("SELECT DISTINCT new com.example.ourknowledgebackend.model.KnownTechnology(t.id, t.name, t.parentId, t.relevant, k.id, k.mainSkill, k.likeIt, null) " +
            "FROM Technology t " +
            "LEFT JOIN Knowledge k ON t.id = k.technology.id and k.user.id = :userId " +
            "WHERE t.relevant = true or k.user.id = :userId")
    List<KnownTechnology> findTechnologiesWithKnowledge(@Param("userId") Long userId);

    @Query("SELECT DISTINCT new com.example.ourknowledgebackend.model.UsesTechnology(t.id, t.name, t.parentId, t.relevant, u.id, null) " +
            "FROM Technology t " +
            "LEFT JOIN Uses u ON t.id = u.technology.id and u.project.id = :projectId " +
            "WHERE t.relevant = true or u.project.id = :projectId")
    List<UsesTechnology> findTechnologiesWithUses(@Param("projectId") Long projectId);
}
