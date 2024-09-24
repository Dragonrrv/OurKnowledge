package com.example.ourknowledgebackend.model.entities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FilterDao extends CrudRepository<Filter, Long> {

    Optional<Filter> findByName(String name);

    List<Filter> findByUser(User user);

    Optional<Filter> findByUserAndName(User user, String defaultFilter);

    List<Filter> findByUserAndNameNot(User user, String name);
}
