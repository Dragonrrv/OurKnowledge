package com.example.ourknowledgebackend.model.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FilterParamDao extends CrudRepository<FilterParam, Long> {
    List<FilterParam> findAllByFilter(Filter filter);

    boolean existsByFilterAndTechnology(Filter filter, Technology technology);

    List<FilterParam> findAllByFilterAndTechnologyParentId(Filter filter, Long parentId);

    Optional<FilterParam> findByFilterAndTechnologyId(Filter filter, Long parentId);
}
