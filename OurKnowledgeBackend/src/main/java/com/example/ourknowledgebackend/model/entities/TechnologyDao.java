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
}
