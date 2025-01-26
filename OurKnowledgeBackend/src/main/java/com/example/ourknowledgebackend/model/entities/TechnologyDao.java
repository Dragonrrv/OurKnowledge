package com.example.ourknowledgebackend.model.entities;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TechnologyDao extends CrudRepository<Technology, Long> {

    List<Technology> findAllByParentId(Long parentId);

    List<Technology> findAllByRelevantTrue();

    Optional<Technology> findByNameAndParentId(String name, Long parentId);

    @Query("SELECT t FROM Technology t WHERE t.relevant is TRUE AND LOWER(t.name) IN :names")
    List<Technology> findAllByRelevantTrueAndNameInIgnoreCase(@Param("names") List<String> names);
}
