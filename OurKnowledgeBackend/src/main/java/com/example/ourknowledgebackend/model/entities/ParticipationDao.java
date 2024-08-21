package com.example.ourknowledgebackend.model.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ParticipationDao extends CrudRepository<Participation, Long> {
    List<Participation> findAllByUser(User user);

    Optional<Participation> findByUserAndProjectAndEndDateIsNull(User user, Project project);

    List<Participation> findAllByProject(Project project);
}
