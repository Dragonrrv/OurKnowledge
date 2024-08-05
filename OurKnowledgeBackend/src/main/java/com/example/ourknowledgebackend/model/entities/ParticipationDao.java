package com.example.ourknowledgebackend.model.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface ParticipationDao extends CrudRepository<Participation, Long> {
    List<Participation> findAllByUser(User user);
}
